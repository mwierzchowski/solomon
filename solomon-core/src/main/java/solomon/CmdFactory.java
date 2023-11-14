package solomon;

import lombok.Setter;
import solomon.flows.RunnableFlow;
import solomon.flows.SupplierFlow;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon.utils.Instances.instantiate;

@Setter
public class CmdFactory {
    private List<Decorator<Object, Object>> globalDecorators;

    public <C extends Runnable> RunnableFlow<C> createRunnable(Class<C> clazz) {
        C command = instantiate(clazz);
        return new RunnableFlow<>(command, this.globalDecorators);
    }

    public <C extends Runnable> RunnableFlow<C> createRunnable(Class<C> clazz, Consumer<C> initializer) {
        return createRunnable(clazz).setup(initializer);
    }

    public <C extends Supplier<O>, O> SupplierFlow<C, O> createSupplier(Class<C> clazz) {
        C command = instantiate(clazz);
        return new SupplierFlow<>(command, this.globalDecorators);
    }

    public <C extends Supplier<O>, O> SupplierFlow<C, O> createSupplier(Class<C> clazz, Consumer<C> initializer) {
        return createSupplier(clazz).setup(initializer);
    }
}