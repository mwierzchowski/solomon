package solomon2.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon2.spi.CmdHandler;
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

@Slf4j
@RequiredArgsConstructor
public class Flow<C, V> extends Context<C> {
    @Getter
    private final C command;
    private final CmdHandler<C, V> cmdHandler;
    private final ConfigSet configSet;

    public V execute() {
        LOG.debug("Execution started");
        Result<V> result = null;
        int decoratorPosition = 0;
        boolean decoratorFailed = true;
        try {
            LOG.debug("Decorating before");
            for (var decorator : this.configSet.decorators()) {
                decoratorPosition += 1;
                decorator.before(super.cast());
            }
            LOG.debug("Executed {} decorators", decoratorPosition);
            decoratorFailed = false;
            LOG.debug("Running command");
            result = cmdHandler.apply(command);
        } catch (RuntimeException ex) {
            if (decoratorFailed) {
                LOG.debug("Exception in decorator on position {}: {}", decoratorPosition, ex.getMessage());
            } else {
                LOG.debug("Exception in command: {}", ex.getMessage());
            }
            result = new Result<>(ex);
        } finally {
            assert result != null;
            LOG.debug("Decorating after");
            for(var decorator : this.configSet.decoratorsReversed(decoratorPosition)) {
                decorator.safeAfter(super.cast(), result.cast());
            }
        }
        int listenerCount = 0;
        if (result.isSuccess()) {
            LOG.debug("Sending success notification(s)");
            for (var listener : this.configSet.listeners()) {
                listener.safeOnSuccess(this.command, result.getValue());
                listenerCount += 1;
            }
        } else {
            LOG.debug("Sending failure notification(s)");
            for (var listener : this.configSet.listeners()) {
                listener.safeOnError(this.command, result.getException());
                listenerCount += 1;
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

    public Flow<C, V> setup(Consumer<C> initializer) {
        LOG.debug("Configuring command");
        initializer.accept(this.command);
        return this;
    }

    public Flow<C, V> setupContext(Consumer<Context<C>> ctxInitializer) {
        LOG.debug("Configuring context");
        ctxInitializer.accept(this);
        return this;
    }

    public Flow<C, V> decorate(Decorator<? super C, ? super V> decorator) {
        this.configSet.addDecorator(decorator);
        return this;
    }

    public Flow<C, V> decorate(Supplier<Decorator<? super C, ? super V>> decoratorSupplier) {
        LOG.debug("Calling decorator supplier");
        this.configSet.addDecorator(decoratorSupplier.get());
        return this;
    }

    public Flow<C, V> decorateBefore(Consumer<Context<? super C>> decoratorMethod) {
        this.configSet.addDecorator(before(decoratorMethod));
        return this;
    }

    public Flow<C, V> decorateAfter(BiConsumer<Context<? super C>, Result<? super V>> decoratorMethod) {
        this.configSet.addDecorator(after(decoratorMethod));
        return this;
    }

    public Flow<C, V> listen(Listener<? super C, ? super V> listener) {
        this.configSet.addListener(listener);
        return this;
    }

    public Flow<C, V> listen(Supplier<Listener<? super C, ? super V>> listenerSupplier) {
        LOG.debug("Calling listener supplier");
        this.configSet.addListener(listenerSupplier.get());
        return this;
    }

    public Flow<C, V> listenOnSuccess(BiConsumer<? super C, ? super V> listenerMethod) {
        this.configSet.addListener(onSuccess(listenerMethod));
        return this;
    }

    public Flow<C, V> listenOnFailure(BiConsumer<? super C, RuntimeException> listenerMethod) {
        this.configSet.addListener(onFailure(listenerMethod));
        return this;
    }
}
