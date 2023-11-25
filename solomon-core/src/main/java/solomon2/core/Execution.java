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
        int decoratorIndex = 0;
        boolean decoratorFailed = true;
        try {
            LOG.debug("Decorating before");
            for (; this.config.contains(Decorator.class, decoratorIndex); decoratorIndex++) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, decoratorIndex);
                decorator.before(cast(this));
            }
            LOG.debug("Executed {} decorators", decoratorIndex);
            decoratorFailed = false;
            LOG.debug("Running command");
            result = commandHandler.apply(command);
        } catch (RuntimeException ex) {
            if (decoratorFailed) {
                LOG.debug("Exception in decorator on position {}: {}", decoratorIndex, ex.getMessage());
                // Increase index to include failed decorator in the command finalization
                decoratorIndex += 1;
            } else {
                LOG.debug("Exception in command: {}", ex.getMessage());
            }
            result = new Result<>(ex);
        } finally {
            assert result != null;
            LOG.debug("Decorating after");
            for (int i = decoratorIndex - 1; this.config.contains(Decorator.class, i); i--) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.safeAfter(cast(this), cast(result));
            }
        }
        int listenerIndex = 0;
        if (result.isSuccess()) {
            LOG.debug("Sending success notification(s)");
            for (; this.config.contains(Listener.class, listenerIndex); listenerIndex++) {
                Listener<?, ?> listener = this.config.get(Listener.class, listenerIndex);
                listener.safeOnSuccess(cast(this.command), cast(result.getValue()));
            }
        } else {
            LOG.debug("Sending failure notification(s)");
            for (; this.config.contains(Listener.class, listenerIndex); listenerIndex++) {
                Listener<?, ?> listener = this.config.get(Listener.class, listenerIndex);
                listener.safeOnError(cast(this.command), result.getException());
            }
            LOG.debug("Sent {} notifications", listenerIndex);
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
