package solomonx.support;

import solomonx.api.Context;
import solomonx.api.Decorator;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(Context<C, V> context) {}

    @Override
    public void after(Context<C, V> context) {}
}
