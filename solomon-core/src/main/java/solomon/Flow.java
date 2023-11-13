package solomon;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.decorators.ExceptionMappingDecorator;
import solomon.decorators.NotificationDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static solomon.utils.Iterables.join;
import static solomon.utils.Iterables.joinReverse;
import static solomon.utils.Predicates.predicateBy;

@Slf4j
@RequiredArgsConstructor
public abstract class Flow<F, C, V> {
    protected final @NonNull C command;
    protected final List<Decorator<Object, Object>> globalDecorators;
    protected List<Decorator<Object, Object>> localDecorators;

    @SuppressWarnings("unchecked")
    public F setup(@NonNull Consumer<C> configurator) {
        configurator.accept(this.command);
        LOG.debug("Command configured");
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F decorate(@NonNull Decorator<?, ?> decorator) {
        if (this.localDecorators == null) {
            this.localDecorators = new ArrayList<>();
        }
        this.localDecorators.add((Decorator<Object, Object>) decorator);
        LOG.debug("Added decorator: {}", decorator);
        return (F) this;
    }

    public F decorateBefore(@NonNull Consumer<C> beforeHandler) {
        var beforeDecorator = new Decorator<C, V>() {
            @Override
            public void before(C command) {
                beforeHandler.accept(command);
            }
        };
        return this.decorate(beforeDecorator);
    }

    public F decorateAfter(@NonNull BiConsumer<C, Result<V>> afterHandler) {
        var afterDecorator = new Decorator<C, V>() {
            @Override
            public void after(C command, Result<V> result) {
                afterHandler.accept(command, result);
            }
        };
        return this.decorate(afterDecorator);
    }

    public <E1 extends RuntimeException, E2 extends RuntimeException> F mapException(Class<E1> srcClass, Class<E2> dstClass) {
        var exceptionMappingDecorator = new ExceptionMappingDecorator(srcClass, dstClass);
        return this.decorate(exceptionMappingDecorator);
    }

    @SuppressWarnings("unchecked")
    public F onSuccess(Consumer<V> listener) {
        findOrCreate(NotificationDecorator.class, NotificationDecorator::new)
                .addSuccessListener(listener);
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F onFailure(Consumer<RuntimeException> listener) {
        findOrCreate(NotificationDecorator.class, NotificationDecorator::new)
                .addFailureListener(listener);
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public V execute() {
        long start = 0;
        Result<V> result = null;
        try {
            for (var decorator : join(this.globalDecorators, this.localDecorators)) {
                decorator.before(this.command);
            }
            start = System.currentTimeMillis();
            result = new Result<>(internalExecute(), start);
        } catch (RuntimeException ex) {
            result = new Result<>(ex, start);
        } finally {
            for (var decorator : joinReverse(this.globalDecorators, this.localDecorators)) {
                decorator.after(this.command, (Result<Object>) result);
            }
        }
        return result.getValueOrThrowException();
    }

    public <T> T execute(@NonNull Function<V, T> valueMapper) {
        V value = execute();
        return valueMapper.apply(value);
    }

    @SuppressWarnings("unchecked")
    protected <D extends Decorator<?, ?>> D findOrCreate(@NonNull Class<D> decoratorClass, Supplier<D> decoratorSupplier) {
        D decorator = null;
        if (this.localDecorators != null) {
            decorator = (D) this.localDecorators.stream()
                    .filter(predicateBy(decoratorClass))
                    .findFirst()
                    .orElse(null);
        }
        if (decorator == null) {
            decorator = decoratorSupplier.get();
            this.decorate(decorator);
        }
        return decorator;
    }

    protected abstract V internalExecute();
}
