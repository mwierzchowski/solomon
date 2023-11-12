package solomon;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.decorators.AfterDecorator;
import solomon.decorators.BeforeDecorator;
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
public abstract class CommandFlow<F, C, R> {
    protected final @NonNull C command;
    protected final List<CommandDecorator> globalDecorators;
    protected List<CommandDecorator> localDecorators;

    @SuppressWarnings("unchecked")
    public F initialize(@NonNull Consumer<C> initializer) {
        initializer.accept(this.command);
        LOG.debug("Command initialized");
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F decorate(@NonNull CommandDecorator decorator) {
        if (this.localDecorators == null) {
            this.localDecorators = new ArrayList<>();
        }
        this.localDecorators.add(decorator);
        LOG.debug("Added decorator: {}", decorator);
        return (F) this;
    }

    public F decorateBefore(@NonNull Consumer<Object> lambdaDecorator) {
        return this.decorate(new BeforeDecorator(lambdaDecorator));
    }

    public F decorateAfter(@NonNull BiConsumer<Object, Result> lambdaDecorator) {
        return this.decorate(new AfterDecorator(lambdaDecorator));
    }

    public <E1 extends RuntimeException, E2 extends RuntimeException> F mapException(Class<E1> srcClass, Class<E2> dstClass) {
        var exceptionMappingDecorator = new ExceptionMappingDecorator(srcClass, dstClass);
        return this.decorate(exceptionMappingDecorator);
    }

    @SuppressWarnings("unchecked")
    public F onSuccess(Consumer<R> listener) {
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

    public R execute() {
        Result result = null;
        long start = 0;
        try {
            for (var decorator : join(this.globalDecorators, this.localDecorators)) {
                decorator.before(this.command);
            }
            start = System.currentTimeMillis();
            result = new Result(internalExecute(), start);
        } catch (RuntimeException ex) {
            result = new Result(ex, start);
        } finally {
            for (var decorator : joinReverse(this.globalDecorators, this.localDecorators)) {
                decorator.after(this.command, result);
            }
        }
        if (result.isFailure()) {
            throw result.getException();
        } else {
            return result.getValue();
        }
    }

    public <T> T execute(@NonNull Function<R, T> resultMapper) {
        R result = execute();
        return resultMapper.apply(result);
    }

    @SuppressWarnings("unchecked")
    protected <D extends CommandDecorator> D findOrCreate(@NonNull Class<D> decoratorClass, Supplier<D> decoratorSupplier) {
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

    protected abstract R internalExecute();
}
