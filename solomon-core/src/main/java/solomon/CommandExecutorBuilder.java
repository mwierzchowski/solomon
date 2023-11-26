package solomon;

import solomon.addons.Addon;
import solomon.services.DefaultFactory;
import solomon.services.DefaultProcessor;
import solomon.services.Factory;
import solomon.services.Processor;

import java.util.ArrayList;
import java.util.List;

public class CommandExecutorBuilder {
    private final List<Addon> registeredAddons = new ArrayList<>();
    private final List<Addon> globalAddons = new ArrayList<>();
    private Config config;
    private Factory factory;
    private Processor processor;

    public CommandExecutorBuilder withFactory(Factory factory) {
        this.factory = factory;
        return this;
    }

    public CommandExecutorBuilder withProcessor(Processor processor) {
        this.processor = processor;
        return this;
    }

    public CommandExecutorBuilder withGlobalConfig(Config config) {
        this.config = config;
        return this;
    }

    public CommandExecutorBuilder withGlobalAddon(Addon addon) {
        this.globalAddons.add(addon);
        return this;
    }

    public CommandExecutorBuilder withRegisteredAddon(Addon addon) {
        this.registeredAddons.add(addon);
        return this;
    }

    public CommandExecutor build() {
        if (this.factory == null) {
            this.factory = new DefaultFactory();
        }
        if (this.processor == null) {
            this.processor = new DefaultProcessor(factory);
        }
        if (this.config == null) {
            this.config = new Config();
        } else if (!globalAddons.isEmpty()) {
            this.config = this.config.unlock();
        }
        this.globalAddons.forEach(this.config::add);
        this.registeredAddons.forEach(this.factory::register);
        return new CommandExecutor(factory, processor, config);
    }
}
