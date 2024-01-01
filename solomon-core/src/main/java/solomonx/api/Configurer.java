package solomonx.api;

import solomonx.utils.Cast;

import java.util.function.Consumer;

public interface Configurer<C, V> {
    default Configurer<C, V> setup(Consumer<C> initializer) {
        initializer.accept(context(this).getCommand());
        return this;
    }

    Output<V> execute();

    private static <C, V> Context<C> context(Configurer<C, V> configurer) {
        return Cast.unchecked(configurer);
    }
}
