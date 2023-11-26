package solomon.addons;

import solomon.Context;
import solomon.Result;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(Context<C> context) {}

    @Override
    public void after(Context<C> context, Result<V> result) {}
}
