package solomon2;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon2.core.Customization;
import solomon2.core.Flow;
import solomon2.spi.CmdHandler;
import solomon2.spi.ConfigProcessor;
import solomon2.spi.CmdFactory;
import solomon2.support.DefaultCmdFactory;
import solomon2.core.Config;
import solomon2.support.NoOpsConfigProcessor;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;
import static solomon2.spi.CmdHandler.RUNNABLE;
import static solomon2.spi.CmdHandler.SUPPLIER;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
public final class Command {
    private static Builder defaultBuilder;

    public static Builder defaultBuilder() {
        if (Command.defaultBuilder == null) {
            var config = new Config();
            var factory = new DefaultCmdFactory();
            var processor = new NoOpsConfigProcessor();
            Command.defaultBuilder = new Builder(config, factory, processor);
        }
        return Command.defaultBuilder;
    }

    @RequiredArgsConstructor
    public static class Builder {
        private final Config config;
        private final CmdFactory factory;
        private final ConfigProcessor processor;

        @SafeVarargs
        public final <C extends Runnable> Flow<C, C> runnable(Class<C> cmdClass, Consumer<C>... initializers) {
            return this.build(cmdClass, RUNNABLE.cast(), initializers);
        }

        @SafeVarargs
        public final <C extends Supplier<V>, V> Flow<C, V> supplier(Class<C> cmdClass, Consumer<C>... initializers) {
            return this.build(cmdClass, SUPPLIER.cast(), initializers);
        }

        protected <C, V> Flow<C, V> build(@NonNull Class<C> cmdClass, CmdHandler<C, V> cmdHandler,
                                          Consumer<C>[] initializers) {
            LOG.debug("Building command: {}", cmdClass.getSimpleName());
            var cmd = this.factory.instantiate(cmdClass);
            var cfgSet = this.processor.process(cmd).ifEmpty(this.config);
            var flow = new Flow<>(cmd, cmdHandler, cfgSet);
            for (var initializer : initializers) {
                flow.setup(initializer);
            }
            return flow;
        }

    }
}
