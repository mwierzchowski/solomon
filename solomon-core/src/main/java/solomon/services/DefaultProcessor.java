package solomon.services;

import lombok.RequiredArgsConstructor;
import solomon.Config;
import solomon.annotations.CommandConfig;

@RequiredArgsConstructor
public class DefaultProcessor implements Processor {
    protected final Factory factory;

    @Override
    public Config process(Object command, Config globalConfig) {
        var metaConfig = command.getClass().getAnnotation(CommandConfig.class);
        if (metaConfig == null) {
            return globalConfig;
        }
        var decorators = metaConfig.decorators();
        var listeners = metaConfig.listeners();
        if (decorators.length == 0 && listeners.length == 0) {
            return globalConfig;
        }
        var config = globalConfig.unlock();
        for (int i = 0; i < decorators.length; i++) {
            config.add(factory.getInstanceOf(decorators[i]));
        }
        for (int i = 0; i < listeners.length; i++) {
            config.add(factory.getInstanceOf(listeners[i]));
        }
        return config;
    }
}
