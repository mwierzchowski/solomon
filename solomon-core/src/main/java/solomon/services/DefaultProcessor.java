package solomon.services;

import lombok.RequiredArgsConstructor;
import solomon.Config;
import solomon.addons.Addon;
import solomon.annotations.Decorated;
import solomon.annotations.Observed;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class DefaultProcessor implements Processor {
    protected final ConcurrentMap<Class<?>, Config> configCache = new ConcurrentHashMap<>();
    protected final Factory factory;

    @Override
    public Config process(Object command, Config globalConfig) {
        return configCache.computeIfAbsent(command.getClass(), aClass -> processClass(aClass, globalConfig));
    }

    private Config processClass(Class<?> cmdClass, Config globalConfig) {
        var addonList = new ArrayList<Addon>();
        ofNullable(cmdClass.getAnnotation(Decorated.class))
                .map(Decorated::value)
                .map(this::processAnnotationData)
                .ifPresent(addonList::addAll);
        ofNullable(cmdClass.getAnnotation(Observed.class))
                .map(Observed::value)
                .map(this::processAnnotationData)
                .ifPresent(addonList::addAll);
        if (addonList.isEmpty()) {
            return globalConfig;
        }
        var config = new Config(globalConfig);
        config.addAll(addonList);
        config.lock();
        return config;
    }

    private List<Addon> processAnnotationData(Class<? extends Addon>[] addonClasses) {
        var addonList = new ArrayList<Addon>();
        for (Class<? extends Addon> addonClass : addonClasses) {
            addonList.add(factory.getInstanceOf(addonClass));
        }
        return addonList;
    }
}
