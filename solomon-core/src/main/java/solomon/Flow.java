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
    Config getConfig();
    void setConfig(Config config);

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
        return this.updateConfig(decorator);
    }

    default Flow<C, V> decorate(Supplier<Decorator<? super C, ? super V>> decoratorSupplier) {
        return updateConfig(decoratorSupplier.get());
    }

    default Flow<C, V> decorateBefore(Consumer<Context<? super C>> decoratorMethod) {
        return this.updateConfig(before(decoratorMethod));
    }

    default Flow<C, V> decorateAfter(BiConsumer<Context<? super C>, Result<? super V>> decoratorMethod) {
        return this.updateConfig(after(decoratorMethod));
    }

    default Flow<C, V> listen(Listener<? super C, ? super V> listener) {
        return this.updateConfig(listener);
    }

    default Flow<C, V> listen(Supplier<Listener<? super C, ? super V>> listenerSupplier) {
        return this.updateConfig(listenerSupplier.get());
    }

    default Flow<C, V> listenOnSuccess(BiConsumer<? super C, ? super V> listenerMethod) {
        return this.updateConfig(onSuccess(listenerMethod));
    }

    default Flow<C, V> listenOnFailure(BiConsumer<? super C, RuntimeException> listenerMethod) {
        return this.updateConfig(onFailure(listenerMethod));
    }

    default Flow<C, V> updateConfig(Addon addon) {
        this.setConfig(this.getConfig().unlock());
        this.getConfig().add(addon);
        return this;
    }
}
