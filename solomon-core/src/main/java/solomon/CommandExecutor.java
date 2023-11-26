package solomon;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.configs.Config;
import solomon.spi.Addon;
import solomon.spi.Factory;
import solomon.spi.Handler;
import solomon.spi.Processor;
import solomon.support.CacheableFactory;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon.Utils.cast;
import static solomon.spi.Handler.RUNNABLE;
import static solomon.spi.Handler.SUPPLIER;

@Slf4j
@RequiredArgsConstructor
public class CommandExecutor {
    public static Builder builder() {
        return new Builder();
    }

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
        var command = this.factory.getInstanceOf(commandClass);
        var config = this.processor.process(command, this.globalConfig.chain());
        var execution = new Execution<>(command, handler, config);
        for (int i = 0; i < initializers.length; i++) {
            execution.setup(initializers[i]);
        }
        return execution;
    }

    public static class Builder {
        private Factory factory = CacheableFactory.getInstance();
        private Processor processor = (cmd, cfg) -> cfg;
        private Config config = Config.emptyConfig();

        public Builder withFactory(Factory factory) {
            this.factory = factory;
            return this;
        }

        public Builder withGlobalAddon(Addon addon) {
            this.config = this.config.add(addon);
            return this;
        }

        public Builder withRegisteredAddon(Addon addon) {
            this.factory.register(addon);
            return this;
        }

        public CommandExecutor build() {
            return new CommandExecutor(factory, processor, config);
        }
    }
}