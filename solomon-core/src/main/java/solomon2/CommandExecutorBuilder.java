package solomon2;

import solomon2.core.configs.Config;
import solomon2.spi.CommandFactory;
import solomon2.spi.ConfigProcessor;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;
import solomon2.support.DefaultCommandFactory;
import solomon2.support.NoOpsConfigProcessor;

public class CommandExecutorBuilder {
    private Config config = Config.emptyConfig();
    private CommandFactory factory = new DefaultCommandFactory();
    private ConfigProcessor processor = new NoOpsConfigProcessor();

    public CommandExecutorBuilder withGlobal(Decorator<?, ?> decorator) {
        this.config = this.config.add(Decorator.class, decorator);
        return this;
    }

    public CommandExecutorBuilder withGlobal(Listener<?, ?> listener) {
        this.config = this.config.add(Listener.class, listener);
        return this;
    }

    public CommandExecutor build() {
        return new CommandExecutor(config, factory, processor);
    }
}
