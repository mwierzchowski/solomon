package solomon;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.services.Factory;
import solomon.services.Processor;
import solomon.services.Processor.AnnotationMap;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon.Handler.RUNNABLE;
import static solomon.Handler.SUPPLIER;
import static solomon.Utils.cast;

@Slf4j
public record CommandExecutor(
        Factory factory,
        Processor processor,
        Config globalConfig,
        AnnotationMap annotationMap
) implements Processor.Context {
    public static CommandExecutorBuilder builder() {
        return new CommandExecutorBuilder();
    }

    public void initialize() {
        this.globalConfig.lock();
        LOG.debug("Initialized command executor");
    }

    @SafeVarargs
    public final <C extends Runnable> Flow<C, C> runnable(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.executionFlow(cmdClass, cast(RUNNABLE), initializers);
    }

    @SafeVarargs
    public final <C extends Supplier<V>, V> Flow<C, V> supplier(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.executionFlow(cmdClass, cast(SUPPLIER), initializers);
    }

    private <C, V> Flow<C, V> executionFlow(@NonNull Class<C> commandClass, Handler<C, V> handler,
                                            Consumer<C>[] initializers) {
        LOG.debug("Building command: {}", commandClass.getSimpleName());
        var command = this.factory.getInstanceOf(commandClass);
        var config = this.processor.process(command, this);
        var execution = new Execution<>(this.factory, command, handler, config);
        for (int i = 0; i < initializers.length; i++) {
            execution.setup(initializers[i]);
        }
        return execution;
    }
}