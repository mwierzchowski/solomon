package solomon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import solomon.configs.Config;
import solomon.spi.Context;
import solomon.spi.Decorator;
import solomon.spi.Handler;
import solomon.spi.Listener;
import solomon.support.ContextAdapter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static solomon.support.Decorators.after;
import static solomon.support.Decorators.before;
import static solomon.support.Listeners.onFailure;
import static solomon.support.Listeners.onSuccess;
import static solomon.support.Utils.cast;

@Slf4j
@AllArgsConstructor
public class Execution<C, V> extends ContextAdapter<C> {
    @Getter
    private final C command;
    private final Handler<C, V> handler;
    private Config config;

    public V execute() {
        LOG.debug("Execution started");
        Result<V> result = null;
        int decoratorCount = 0;
        boolean decoratorFailed = true;
        try {
            LOG.debug("Decorating before");
            for (int i = 0; this.config.contains(Decorator.class, i); i++, decoratorCount++) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.before(cast(this));
            }
            LOG.debug("Executed {} decorators", decoratorCount);
            decoratorFailed = false;
            LOG.debug("Running command");
            result = handler.apply(command);
        } catch (RuntimeException ex) {
            if (decoratorFailed) {
                decoratorCount += 1;
                LOG.debug("Exception in decorator on position {}: {}", decoratorCount, ex.getMessage());
            } else {
                LOG.debug("Exception in command: {}", ex.getMessage());
            }
            result = new Result<>(ex);
        } finally {
            assert result != null;
            LOG.debug("Decorating after");
            for (int i = decoratorCount - 1; this.config.contains(Decorator.class, i); i--) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.safeAfter(cast(this), cast(result));
            }
        }
        int listenerCount = 0;
        if (result.isSuccess()) {
            LOG.debug("Sending success notification(s)");
            for (int i = 0; this.config.contains(Listener.class, i); i++, listenerCount++) {
                Listener<?, ?> listener = this.config.get(Listener.class, i);
                listener.safeOnSuccess(cast(this.command), cast(result.getValue()));
            }
        } else {
            LOG.debug("Sending failure notification(s)");
            for (int i = 0; this.config.contains(Listener.class, i); i++) {
                Listener<?, ?> listener = this.config.get(Listener.class, i);
                listener.safeOnError(cast(this.command), result.getException());
            }
            LOG.debug("Sent {} notifications", listenerCount);
        }
        LOG.debug("Execution finished");
        return result.getValueOrThrowException();
    }

    public <T> T execute(Function<V, T> mapper) {
        V value = this.execute();
        LOG.debug("Converting result: {}", mapper);
        return mapper.apply(value);
    }

    public Execution<C, V> setup(Consumer<C> initializer) {
        LOG.debug("Configuring command");
        initializer.accept(this.command);
        return this;
    }

    public Execution<C, V> setupContext(Consumer<Context<C>> ctxInitializer) {
        LOG.debug("Configuring context");
        ctxInitializer.accept(this);
        return this;
    }

    public Execution<C, V> decorate(Decorator<? super C, ? super V> decorator) {
        this.config = this.config.add(decorator);
        return this;
    }

    public Execution<C, V> decorate(Supplier<Decorator<? super C, ? super V>> decoratorSupplier) {
        LOG.debug("Calling decorator supplier");
        return this.decorate(decoratorSupplier.get());
    }

    public Execution<C, V> decorateBefore(Consumer<Context<? super C>> decoratorMethod) {
        this.config = this.config.add(before(decoratorMethod));
        return this;
    }

    public Execution<C, V> decorateAfter(BiConsumer<Context<? super C>, Result<? super V>> decoratorMethod) {
        this.config = this.config.add(after(decoratorMethod));
        return this;
    }

    public Execution<C, V> listen(Listener<? super C, ? super V> listener) {
        this.config = this.config.add(listener);
        return this;
    }

    public Execution<C, V> listen(Supplier<Listener<? super C, ? super V>> listenerSupplier) {
        LOG.debug("Calling listener supplier");
        return this.listen(listenerSupplier.get());
    }

    public Execution<C, V> listenOnSuccess(BiConsumer<? super C, ? super V> listenerMethod) {
        this.config = this.config.add(onSuccess(listenerMethod));
        return this;
    }

    public Execution<C, V> listenOnFailure(BiConsumer<? super C, RuntimeException> listenerMethod) {
        this.config = this.config.add(onFailure(listenerMethod));
        return this;
    }
}
