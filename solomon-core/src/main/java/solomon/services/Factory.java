package solomon.services;

import solomon.addons.Addon;

public interface Factory {
    <C> C getInstanceOf(Class<C> clazz);
    <A extends Addon> void cache(A addon);
}
