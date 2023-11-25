package solomon;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.configs.Config;
import solomon.spi.Addon;
import solomon.spi.Factory;
import solomon.spi.Handler;
import solomon.spi.Processor;
import solomon.support.Utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon.configs.Config.emptyConfig;
import static solomon.spi.Handler.RUNNABLE;
import static solomon.spi.Handler.SUPPLIER;
import static solomon.support.Utils.cast;

@Slf4j
@RequiredArgsConstructor
public class CommandExecutor {
    private final Factory factory;
    private final Processor processor;
    private final Config globalConfig;

    @SafeVarargs
    public final <C extends Runnable> Execution<C, C> runnable(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.kickoff(cmdClass, cast(RUNNABLE), initializers);
    }

    @SafeVarargs
    public final <C extends Supplier<V>, V> Execution<C, V> supplier(Class<C> cmdClass, Consumer<C>... initializers) {
        return this.kickoff(cmdClass, cast(SUPPLIER), initializers);
    }

    protected <C, V> Execution<C, V> kickoff(@NonNull Class<C> commandClass, Handler<C, V> handler,
                                             Consumer<C>[] initializers) {
        LOG.debug("Building command: {}", commandClass.getSimpleName());
        var command = this.factory.instantiate(commandClass);
        var config = this.processor.process(command, this.globalConfig.chain());
        var execution = new Execution<>(command, handler, config);
        for (int i = 0; i < initializers.length; i++) {
            execution.setup(initializers[i]);
        }
        return execution;
    }

    public static class Builder {
        private Factory factory = Utils::newInstanceOf;
        private Processor processor = (cmd, cfg) -> cfg;
        private Config config = emptyConfig();

        public Builder withGlobal(Addon addon) {
            this.config = this.config.add(addon);
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