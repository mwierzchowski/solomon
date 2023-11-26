package solomon.spi;

import solomon.annotation.AddonConfig;

import static solomon.annotation.AddonConfig.Scope.SERVICE;

public interface Addon {
    default boolean isCacheable() {
        var config = getClass().getAnnotation(AddonConfig.class);
        if (config != null) {
            return config.scope() == SERVICE;
        } else {
            return true;
        }
    }
}
