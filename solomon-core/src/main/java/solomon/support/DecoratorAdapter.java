package solomon.support;

import solomon.spi.Context;
import solomon.Result;
import solomon.spi.Decorator;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(Context<C> context) {}

    @Override
    public void after(Context<C> context, Result<V> result) {}
}
