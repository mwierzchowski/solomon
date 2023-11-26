package solomon.addons;

import solomon.annotations.AddonConfig;

import static solomon.annotations.AddonConfig.Scope.SERVICE;

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
