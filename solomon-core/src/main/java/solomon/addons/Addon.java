package solomon.addons;

import java.util.Objects;

import static solomon.annotations.Addon.CacheMode.DEFAULT;

public interface Addon {
    default boolean isCacheable() {
        var config = getClass().getAnnotation(solomon.annotations.Addon.class);
        if (config != null) {
            return Objects.equals(config.cacheMode(), DEFAULT);
        } else {
            return true;
        }
    }
}
