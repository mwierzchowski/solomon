package solomon.addons;

import solomon.ExecutionContext;
import solomon.MutableResult;

public abstract class DecoratorAdapter<C, V> implements Decorator<C, V> {
    @Override
    public void before(ExecutionContext<C> context) {}

    @Override
    public void after(ExecutionContext<C> context, MutableResult<V> result) {}
}
