package solomon2.core.configs;

import solomon2.spi.Addon;

public interface Config {
    static Config emptyConfig() {
        return EmptyConfig.INSTANCE;
    }

    <A extends Addon> A get(Class<A> addonClass, int position);
    Config add(Addon addon);
    int count(Class<? extends Addon> addonClass);
    Config chain();

    default boolean contains(Class<? extends Addon> addonClass, int position) {
        return position >= 0 && position < this.count(addonClass);
    }
}
