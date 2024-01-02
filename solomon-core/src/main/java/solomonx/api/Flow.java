package solomonx.api;

import solomonx.util.Cast;

import java.util.function.Consumer;

public interface Flow<C, V> {
    default Flow<C, V> setup(Consumer<C> initializer) {
        Context<C> context = Cast.unchecked(this);
        initializer.accept(context.getCommand());
        return this;
    }

    Output<V> execute();
}
