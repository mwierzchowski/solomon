package solomonx.support;

import solomonx.api.Context;
import solomonx.api.OutputStore;
import solomonx.api.Decorator;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(Context<C> context) {}

    @Override
    public void after(Context<C> context, OutputStore<V> outputStore) {}
}
