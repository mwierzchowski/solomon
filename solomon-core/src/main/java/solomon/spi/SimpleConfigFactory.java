package solomon.spi;

import solomon.Config;
import solomon.utils.Instances;

import java.util.HashMap;
import java.util.Map;

public class SimpleConfigFactory implements ConfigFactory {
    private Map<Class<?>, Object> cache = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <C extends Config> C create(Class<C> clazz) {
        return (C) cache.computeIfAbsent(clazz, Instances::instantiate);
    }
}
