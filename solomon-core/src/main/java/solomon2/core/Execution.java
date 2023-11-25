package solomon2.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import solomon2.core.configs.Config;
import solomon2.spi.CommandHandler;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static solomon2.Decorators.after;
import static solomon2.Decorators.before;
import static solomon2.Listeners.onFailure;
import static solomon2.Listeners.onSuccess;
import static solomon2.core.Utils.cast;

@Slf4j
@AllArgsConstructor
public class Execution<C, V> extends Context<C> {
    @Getter
    private final C command;
    private final CommandHandler<C, V> commandHandler;
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
            result = commandHandler.apply(command);
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
        this.config = this.config.add(Decorator.class, decorator);
        return this;
    }

    public Execution<C, V> decorate(Supplier<Decorator<? super C, ? super V>> decoratorSupplier) {
        LOG.debug("Calling decorator supplier");
        return this.decorate(decoratorSupplier.get());
    }

    public Execution<C, V> decorateBefore(Consumer<Context<? super C>> decoratorMethod) {
        this.config = this.config.add(Decorator.class, before(decoratorMethod));
        return this;
    }

    public Execution<C, V> decorateAfter(BiConsumer<Context<? super C>, Result<? super V>> decoratorMethod) {
        this.config = this.config.add(Decorator.class, after(decoratorMethod));
        return this;
    }

    public Execution<C, V> listen(Listener<? super C, ? super V> listener) {
        this.config = this.config.add(Listener.class, listener);
        return this;
    }

    public Execution<C, V> listen(Supplier<Listener<? super C, ? super V>> listenerSupplier) {
        LOG.debug("Calling listener supplier");
        return this.listen(listenerSupplier.get());
    }

    public Execution<C, V> listenOnSuccess(BiConsumer<? super C, ? super V> listenerMethod) {
        this.config = this.config.add(Listener.class, onSuccess(listenerMethod));
        return this;
    }

    public Execution<C, V> listenOnFailure(BiConsumer<? super C, RuntimeException> listenerMethod) {
        this.config = this.config.add(Listener.class, onFailure(listenerMethod));
        return this;
    }
}
