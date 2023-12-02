package solomon.addons;

import solomon.annotations.AddonConfig;

public interface Addon {
    default boolean isCacheable() {
        var config = getClass().getAnnotation(AddonConfig.class);
        if (config != null) {
            return config.useCache();
        } else {
            return true;
        }
    }
}
