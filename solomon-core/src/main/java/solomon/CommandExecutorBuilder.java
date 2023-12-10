package solomon;

import solomon.addons.Addon;
import solomon.services.DefaultFactory;
import solomon.services.DefaultProcessor;
import solomon.services.Factory;
import solomon.services.Processor;

import java.util.ArrayList;
import java.util.List;

import static solomon.Utils.cast;

public class CommandExecutorBuilder {
    private final List<Addon> cachedAddons = new ArrayList<>();
    private final List<Object> globalAddons = new ArrayList<>();
    private Config globalConfig;
    private Factory factory;
    private Processor processor;
    private boolean initialize = true;

    public CommandExecutorBuilder withFactory(Factory factory) {
        this.factory = factory;
        return this;
    }

    public CommandExecutorBuilder withProcessor(Processor processor) {
        this.processor = processor;
        return this;
    }

    public CommandExecutorBuilder withGlobalConfig(Config globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }

    public CommandExecutorBuilder withGlobalAddon(Addon addon) {
        this.globalAddons.add(addon);
        return this;
    }

    public CommandExecutorBuilder withGlobalAddon(Class<? extends Addon> addonClass) {
        this.globalAddons.add(addonClass);
        return this;
    }

    public CommandExecutorBuilder withCachedAddon(Addon addon) {
        this.cachedAddons.add(addon);
        return this;
    }

    public CommandExecutorBuilder initialize(boolean initialize) {
        this.initialize = initialize;
        return this;
    }

    public CommandExecutor build() {
        if (this.factory == null) {
            this.factory = new DefaultFactory();
        }
        if (this.processor == null) {
            this.processor = new DefaultProcessor(this.factory);
        }
        if (this.globalConfig == null) {
            this.globalConfig = new Config();
        }
        this.cachedAddons.forEach(this.factory::cache);
        for (var addon : globalAddons) {
            if (addon instanceof Class<?> addonClass) {
                addon = this.factory.getInstanceOf(addonClass);
            }
            this.globalConfig.add(cast(addon));
        }
        var executor = new CommandExecutor(this.factory, this.processor, this.globalConfig);
        if (this.initialize) {
            executor.initialize();
        }
        return executor;
    }
}
