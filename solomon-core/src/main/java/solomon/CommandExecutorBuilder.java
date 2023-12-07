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

    public CommandExecutorBuilder withConfig(Config config) {
        this.config = config;
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

    public CommandExecutor build() {
        if (this.factory == null) {
            this.factory = new DefaultFactory();
        }
        if (this.processor == null) {
            this.processor = new DefaultProcessor(this.factory);
        }
        if (this.config == null) {
            this.config = new Config();
        }
        this.cachedAddons.forEach(this.factory::cache);
        for (var addon : globalAddons) {
            if (addon instanceof Class<?> addonClass) {
                addon = this.factory.getInstanceOf(addonClass);
            }
            this.config.add(cast(addon));
        }
        var executor = new CommandExecutor();
        executor.setFactory(this.factory);
        executor.setProcessor(this.processor);
        executor.setGlobalConfig(this.config);
        return executor;
    }
}
