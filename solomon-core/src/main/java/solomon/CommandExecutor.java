package solomon;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.services.Factory;
import solomon.services.Processor;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon.Handler.RUNNABLE;
import static solomon.Handler.SUPPLIER;
import static solomon.Utils.cast;

@Slf4j
public class CommandExecutor {
    public static CommandExecutorBuilder builder() {
        return new CommandExecutorBuilder();
    }

    private final Factory factory;
    private final Processor processor;
    private final Config globalConfig;

    public CommandExecutor(Factory factory, Processor processor, Config globalConfig) {
        this.factory = factory;
        this.processor = processor;
        this.globalConfig = globalConfig;
        this.globalConfig.lock();
    }

    @SafeVarargs
    public final <C extends Runnable> Flow<C, C> runnable(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.kickoff(cmdClass, cast(RUNNABLE), initializers);
    }

    @SafeVarargs
    public final <C extends Supplier<V>, V> Flow<C, V> supplier(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.kickoff(cmdClass, cast(SUPPLIER), initializers);
    }

    protected <C, V> Flow<C, V> kickoff(@NonNull Class<C> commandClass, Handler<C, V> handler,
                                             Consumer<C>[] initializers) {
        LOG.debug("Building command: {}", commandClass.getSimpleName());
        var command = this.factory.getInstanceOf(commandClass);
        var config = this.processor.process(command, this.globalConfig);
        var execution = new Execution<C, V>();
        execution.setCommand(command);
        execution.setHandler(cast(handler));
        execution.setConfig(config);
        for (int i = 0; i < initializers.length; i++) {
            execution.setup(initializers[i]);
        }
        return execution;
    }
}