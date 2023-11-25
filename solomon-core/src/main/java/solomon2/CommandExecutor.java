package solomon2;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon2.spi.Addon;
import solomon2.core.Execution;
import solomon2.core.configs.Config;
import solomon2.spi.CommandFactory;
import solomon2.spi.CommandHandler;
import solomon2.spi.ConfigProcessor;
import solomon2.support.DefaultCommandFactory;
import solomon2.support.NoOpsConfigProcessor;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon2.core.Utils.cast;
import static solomon2.spi.CommandHandler.RUNNABLE;
import static solomon2.spi.CommandHandler.SUPPLIER;

@Slf4j
@RequiredArgsConstructor
public class CommandExecutor {
    private final CommandFactory factory;
    private final ConfigProcessor processor;
    private final Config config;

    @SafeVarargs
    public final <C extends Runnable> Execution<C, C> runnable(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.execution(cmdClass, cast(RUNNABLE), initializers);
    }

    @SafeVarargs
    public final <C extends Supplier<V>, V> Execution<C, V> supplier(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.execution(cmdClass, cast(SUPPLIER), initializers);
    }

    protected <C, V> Execution<C, V> execution(@NonNull Class<C> commandClass, CommandHandler<C, V> commandHandler,
                                               Consumer<C>[] initializers) {
        LOG.debug("Building command: {}", commandClass.getSimpleName());
        var command = this.factory.instantiate(commandClass);
        var config = this.processor.process(command, this.config.chain());
        var execution = new Execution<>(command, commandHandler, config);
        for (var initializer : initializers) {
            execution.setup(initializer);
        }
        return execution;
    }

    static class Builder {
        private CommandFactory factory = new DefaultCommandFactory();
        private ConfigProcessor processor = new NoOpsConfigProcessor();
        private Config config = Config.emptyConfig();

        public Builder withGlobal(Addon addon) {
            this.config = this.config.addAddon(addon);
            return this;
        }

        public Builder using(Addon addon) {
            throw new UnsupportedOperationException("Method not implemented");
        }

        public CommandExecutor build() {
            return new CommandExecutor(factory, processor, config);
        }
    }
}