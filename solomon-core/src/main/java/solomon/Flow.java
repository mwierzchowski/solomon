package solomon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solomon.addons.Addon;
import solomon.addons.Decorator;
import solomon.addons.Listener;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static solomon.addons.Decorators.after;
import static solomon.addons.Decorators.before;
import static solomon.addons.Listeners.onFailure;
import static solomon.addons.Listeners.onSuccess;

public interface Flow<C, V> {
    Logger LOG = LoggerFactory.getLogger(Flow.class);

    V execute();
    C getCommand();
    void updateConfig(Addon addon);

    default <T> T execute(Function<V, T> mapper) {
        V value = this.execute();
        LOG.debug("Converting result: {}", mapper);
        return mapper.apply(value);
    }

    default Flow<C, V> setup(Consumer<C> initializer) {
        LOG.debug("Configuring command");
        initializer.accept(this.getCommand());
        return this;
    }

    default Flow<C, V> decorate(Decorator<? super C, ? super V> decorator) {
        this.updateConfig(decorator);
        return this;
    }

    default Flow<C, V> decorate(Supplier<Decorator<? super C, ? super V>> decoratorSupplier) {
        updateConfig(decoratorSupplier.get());
        return this;
    }

    default Flow<C, V> decorateBefore(Consumer<Context<? super C>> decoratorMethod) {
        this.updateConfig(before(decoratorMethod));
        return this;
    }

    default Flow<C, V> decorateAfter(BiConsumer<Context<? super C>, Result<? super V>> decoratorMethod) {
        this.updateConfig(after(decoratorMethod));
        return this;
    }

    default Flow<C, V> listen(Listener<? super C, ? super V> listener) {
        this.updateConfig(listener);
        return this;
    }

    default Flow<C, V> listen(Supplier<Listener<? super C, ? super V>> listenerSupplier) {
        this.updateConfig(listenerSupplier.get());
        return this;
    }

    default Flow<C, V> listenOnSuccess(BiConsumer<? super C, ? super V> listenerMethod) {
        this.updateConfig(onSuccess(listenerMethod));
        return this;
    }

    default Flow<C, V> listenOnFailure(BiConsumer<? super C, RuntimeException> listenerMethod) {
        this.updateConfig(onFailure(listenerMethod));
        return this;
    }
}
