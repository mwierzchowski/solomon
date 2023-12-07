package solomon.services;

import lombok.RequiredArgsConstructor;
import solomon.Config;
import solomon.addons.Addon;
import solomon.annotations.Decorated;
import solomon.annotations.Observed;

@RequiredArgsConstructor
public class DefaultProcessor implements Processor {
    protected final Factory factory;

    @Override
    public Config process(Object command, Config globalConfig) {
        var decorationData = command.getClass().getAnnotation(Decorated.class);
        var observationData = command.getClass().getAnnotation(Observed.class);
        if ((decorationData == null || decorationData.value() == null)
                && (observationData == null || observationData.value() == null)) {
            return globalConfig;
        }
        var config = globalConfig.unlock();
        process(decorationData.value(), config);
        process(observationData.value(), config);
        return config;
    }

    private void process(Class<? extends Addon>[] addonClasses, Config config) {
        for (int i = 0; i < addonClasses.length; i++) {
            var addonClass = addonClasses[i];
            var addon = factory.getInstanceOf(addonClass);
            config.add(addon);
        }
    }
}
