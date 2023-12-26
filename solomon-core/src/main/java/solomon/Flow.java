package solomon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solomon.addons.Addon;
import solomon.addons.Decorator;
import solomon.addons.Observer;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static solomon.addons.Decorators.after;
import static solomon.addons.Decorators.before;
import static solomon.addons.Observers.onFailure;
import static solomon.addons.Observers.onSuccess;

public interface Flow<C, V> extends CommandAware<C> {
    Logger LOG = LoggerFactory.getLogger(Flow.class);

    Config getConfig(boolean forUpdate);
    Addon getAddon(Class<? extends Addon> addonClass);
    V execute();

    default Flow<C, V> setup(Consumer<C> initializer) {
        LOG.debug("Configuring command");
        initializer.accept(this.getCommand());
        return this;
    }

    default Flow<C, V> decorate(Class<? extends Decorator<? super C, ? super V>> decoratorClass) {
        getConfig(true).add(getAddon(decoratorClass));
        return this;
    }

    default Flow<C, V> decorate(Decorator<? super C, ? super V> decorator) {
        getConfig(true).add(decorator);
        return this;
    }

    default Flow<C, V> decorateBefore(Consumer<ExecutionContext<? super C>> decoratorMethod) {
        getConfig(true).add(before(decoratorMethod));
        return this;
    }

    default Flow<C, V> decorateAfter(BiConsumer<ExecutionContext<? super C>, Result<? super V>> decoratorMethod) {
        getConfig(true).add(after(decoratorMethod));
        return this;
    }

    default Flow<C, V> observe(Class<? extends Observer<? super C, ? super V>> observerClass) {
        getConfig(true).add(getAddon(observerClass));
        return this;
    }

    default Flow<C, V> observe(Observer<? super C, ? super V> observer) {
        getConfig(true).add(observer);
        return this;
    }

    default Flow<C, V> observeSuccess(BiConsumer<? super C, ? super V> observerMethod) {
        getConfig(true).add(onSuccess(observerMethod));
        return this;
    }

    default Flow<C, V> observeFailure(BiConsumer<? super C, RuntimeException> observerMethod) {
        getConfig(true).add(onFailure(observerMethod));
        return this;
    }

    default <T> T execute(Function<V, T> mapper) {
        V value = this.execute();
        LOG.debug("Converting result: {}", mapper);
        return mapper.apply(value);
    }
}
