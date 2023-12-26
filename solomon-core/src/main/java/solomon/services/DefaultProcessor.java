package solomon.services;

import lombok.RequiredArgsConstructor;
import solomon.Config;
import solomon.addons.Addon;
import solomon.annotations.Decorated;
import solomon.annotations.Observed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public class DefaultProcessor implements Processor {
    protected final ConcurrentMap<Class<?>, Config> configCache = new ConcurrentHashMap<>();

    @Override
    public Config process(Object command, Context context) {
        return configCache.computeIfAbsent(command.getClass(), aClass -> processClass(aClass, context));
    }

    private Config processClass(Class<?> cmdClass, Context context) {
        var addonList = new ArrayList<Addon>();
        stream(cmdClass.getAnnotations())
                .filter(context.annotationMap()::containsKey)
                .map(context.annotationMap()::get)
                .map(context.factory()::getInstanceOf)
                .forEach(addonList::add);
        stream(cmdClass.getAnnotations())
                .filter(annotation -> Decorated.class.isAssignableFrom(annotation.annotationType()))
                .map(annotation -> ((Decorated)annotation).value())
                .flatMap(Arrays::stream)
                .map(context.factory()::getInstanceOf)
                .forEach(addonList::add);
        stream(cmdClass.getAnnotations())
                .filter(annotation -> Observed.class.isAssignableFrom(annotation.annotationType()))
                .map(annotation -> ((Observed)annotation).value())
                .flatMap(Arrays::stream)
                .map(context.factory()::getInstanceOf)
                .forEach(addonList::add);
        if (addonList.isEmpty()) {
            return context.globalConfig();
        }
        var config = new Config(context.globalConfig());
        config.addAll(addonList);
        config.lock();
        return config;
    }
}

