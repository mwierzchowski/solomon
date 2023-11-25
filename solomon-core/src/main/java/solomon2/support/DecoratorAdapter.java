package solomon2.support;

import solomon2.spi.Context;
import solomon2.core.Result;
import solomon2.spi.Decorator;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(Context<C> context) {}

    @Override
    public void after(Context<C> context, Result<V> result) {}
}
