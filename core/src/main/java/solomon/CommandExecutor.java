package solomon;

import solomon.flows.CommandFlowRunnable;
import solomon.flows.CommandFlowSupplier;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class CommandExecutor {
    protected abstract <C> C instantiateCommand(Class<C> clazz);

    public <C extends Runnable> CommandFlowRunnable<C> runnable(Class<C> clazz) {
        C command = instantiateCommand(clazz);
        return new CommandFlowRunnable<>(command);
    }

    public <C extends Runnable> CommandFlowRunnable<C> runnable(Class<C> clazz, Consumer<C> initializer) {
        return runnable(clazz).initialize(initializer);
    }

    public <C extends Supplier<O>, O> CommandFlowSupplier<C, O> supplier(Class<C> clazz) {
        C command = instantiateCommand(clazz);
        return new CommandFlowSupplier<>(command);
    }

    public <C extends Supplier<O>, O> CommandFlowSupplier<C, O> supplier(Class<C> clazz, Consumer<C> initializer) {
        return supplier(clazz).initialize(initializer);
    }
}