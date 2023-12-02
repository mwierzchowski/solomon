package solomon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

public interface Flow<C, V> extends CommandAware<C> {
    Logger LOG = LoggerFactory.getLogger(Flow.class);

    Config getConfig(boolean forUpdate);
    V execute();

    default Flow<C, V> setup(Consumer<C> initializer) {
        LOG.debug("Configuring command");
        initializer.accept(this.getCommand());
        return this;
    }

    default Flow<C, V> decorate(Decorator<? super C, ? super V> decorator) {
        getConfig(true).add(decorator);
        return this;
    }

    default Flow<C, V> decorate(Supplier<Decorator<? super C, ? super V>> decoratorSupplier) {
        getConfig(true).add(decoratorSupplier.get());
        return this;
    }

    default Flow<C, V> decorateBefore(Consumer<Context<? super C>> decoratorMethod) {
        getConfig(true).add(before(decoratorMethod));
        return this;
    }

    default Flow<C, V> decorateAfter(BiConsumer<Context<? super C>, Result<? super V>> decoratorMethod) {
        getConfig(true).add(after(decoratorMethod));
        return this;
    }

    default Flow<C, V> listen(Listener<? super C, ? super V> listener) {
        getConfig(true).add(listener);
        return this;
    }

    default Flow<C, V> listen(Supplier<Listener<? super C, ? super V>> listenerSupplier) {
        getConfig(true).add(listenerSupplier.get());
        return this;
    }

    default Flow<C, V> listenOnSuccess(BiConsumer<? super C, ? super V> listenerMethod) {
        getConfig(true).add(onSuccess(listenerMethod));
        return this;
    }

    default Flow<C, V> listenOnFailure(BiConsumer<? super C, RuntimeException> listenerMethod) {
        getConfig(true).add(onFailure(listenerMethod));
        return this;
    }

    default <T> T execute(Function<V, T> mapper) {
        V value = this.execute();
        LOG.debug("Converting result: {}", mapper);
        return mapper.apply(value);
    }
}
