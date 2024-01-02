package solomonx.spi;

import solomon.addons.Addon;

import java.util.HashMap;
import java.util.Map;

import static solomon.Utils.cast;
import static solomon.Utils.newInstanceOf;

public class DefaultFactory implements Factory {
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
    public <A extends Addon> void cache(A addon) {
        addonCache.put(addon.getClass(), cast(addon));
    }

    protected <T> T addonInstanceOf(Class<? extends Addon> addonClass) {
        var addon = addonCache.get(addonClass);
        if (addon == null) {
            addon = newInstanceOf(addonClass);
            addonCache.put(addonClass, cast(addon));
        }
        return cast(addon);
    }
}
