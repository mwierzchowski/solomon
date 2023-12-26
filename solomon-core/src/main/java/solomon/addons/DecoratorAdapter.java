package solomon.addons;

import solomon.ExecutionContext;
import solomon.Result;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(ExecutionContext<C> context) {}

    @Override
    public void after(ExecutionContext<C> context, Result<V> result) {}
}
