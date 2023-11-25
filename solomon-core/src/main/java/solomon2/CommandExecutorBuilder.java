package solomon2;

import solomon2.core.Addon;
import solomon2.core.configs.Config;
import solomon2.spi.CommandFactory;
import solomon2.spi.ConfigProcessor;
import solomon2.support.DefaultCommandFactory;
import solomon2.support.NoOpsConfigProcessor;

public class CommandExecutorBuilder {
    private Config config = Config.emptyConfig();
    private CommandFactory factory = new DefaultCommandFactory();
    private ConfigProcessor processor = new NoOpsConfigProcessor();

    public CommandExecutorBuilder withGlobal(Addon addon) {
        this.config = this.config.addAddon(addon);
        return this;
    }

    public CommandExecutorBuilder using(Addon addon) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    public CommandExecutor build() {
        return new CommandExecutor(config, factory, processor);
    }
}
