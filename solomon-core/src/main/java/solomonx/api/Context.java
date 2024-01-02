package solomonx.api;

public interface Context<C, V> extends OutputStore<V> {
    C getCommand();
}
