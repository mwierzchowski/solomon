package solomon.addons;

public interface Addon {
    default boolean isCacheable() {
        var config = getClass().getAnnotation(solomon.annotations.Addon.class);
        if (config != null) {
            return config.useCache();
        } else {
            return true;
        }
    }
}
