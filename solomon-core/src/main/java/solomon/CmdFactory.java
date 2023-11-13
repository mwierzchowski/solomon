package solomon;

import lombok.Setter;
import solomon.flows.RunnableFlow;
import solomon.flows.SupplierFlow;

import java.text.MessageFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Setter
public class CmdFactory {
    private List<Decorator<Object, Object>> globalDecorators;

    public <C extends Runnable> RunnableFlow<C> createRunnable(Class<C> clazz) {
        C command = instantiateCommand(clazz);
        return new RunnableFlow<>(command, this.globalDecorators);
    }

    public <C extends Runnable> RunnableFlow<C> createRunnable(Class<C> clazz, Consumer<C> initializer) {
        return createRunnable(clazz).setup(initializer);
    }

    public <C extends Supplier<O>, O> SupplierFlow<C, O> createSupplier(Class<C> clazz) {
        C command = instantiateCommand(clazz);
        return new SupplierFlow<>(command, this.globalDecorators);
    }

    public <C extends Supplier<O>, O> SupplierFlow<C, O> createSupplier(Class<C> clazz, Consumer<C> initializer) {
        return createSupplier(clazz).setup(initializer);
    }

    protected <C> C instantiateCommand(Class<C> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            var msg = MessageFormat.format("Could not instantiate {0}", clazz);
            throw new IllegalArgumentException(msg, ex);
        }
    }
}