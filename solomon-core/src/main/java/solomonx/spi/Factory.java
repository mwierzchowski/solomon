package solomonx.spi;

import solomon.addons.Addon;

public interface Factory {
    <C> C getInstanceOf(Class<C> clazz);
    <A extends Addon> void cache(A addon); // TODO rethink this idiom
}
