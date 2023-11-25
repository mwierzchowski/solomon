package solomon2.core.configs;

import solomon2.core.Addon;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

import static solomon2.core.Utils.cast;

public interface Config {
    static Config emptyConfig() {
        return EmptyConfig.INSTANCE;
    }

    <T extends Addon> T get(Class<T> clazz, int index);
    <T extends Addon> Config add(Class<T> clazz, T item);
    int size(Class<? extends Addon> clazz);
    Config chain();

    default boolean contains(Class<? extends Addon> clazz, int index) {
        return index >= 0 && index < this.size(clazz);
    }

    default <T extends Addon> Config add(T item) {
        Class<T> clazz = null;
        if (item instanceof Decorator) {
            clazz = cast(Decorator.class);
        } else if (item instanceof Listener) {
            clazz = cast(Listener.class);
        }
        return this.add(clazz, item);
    }
}
