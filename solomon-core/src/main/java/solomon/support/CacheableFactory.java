package solomon.support;

import lombok.NoArgsConstructor;
import solomon.spi.Addon;
import solomon.spi.Factory;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
import static solomon.Utils.cast;
import static solomon.Utils.newInstanceOf;

@NoArgsConstructor(access = PRIVATE)
public class CacheableFactory implements Factory {
    protected static CacheableFactory instance;

    public static CacheableFactory getInstance() {
        if (instance == null) {
            instance = new CacheableFactory();
        }
        return instance;
    }

    protected Map<Class<? extends Addon>, ? extends Addon> addonCache = new HashMap<>();

    @Override
    public <T> T getInstanceOf(Class<T> clazz) {
        if (Addon.class.isAssignableFrom(clazz)) {
            return addonInstanceOf(cast(clazz));
        } else {
            return newInstanceOf(clazz);
        }
    }

    @Override
    public <A extends Addon> void register(A addon) {
        addonCache.put(addon.getClass(), cast(addon));
    }

    protected <T> T addonInstanceOf(Class<? extends Addon> addonClass) {
        var addon = addonCache.get(addonClass);
        if (addon == null) {
            addon = newInstanceOf(addonClass);
            if (addon.isCacheable()) {
                addonCache.put(addonClass, cast(addon));
            }
        }
        return cast(addon);
    }
}
