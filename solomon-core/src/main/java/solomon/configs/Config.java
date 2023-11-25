package solomon.configs;

import solomon.spi.Addon;

public interface Config {
    static Config emptyConfig() {
        return EmptyConfig.INSTANCE;
    }

    Config add(Addon addon);
    <A extends Addon> A get(Class<A> addonClass, int position);
    int count(Class<? extends Addon> addonClass);
    Config chain();

    default boolean contains(Class<? extends Addon> addonClass, int position) {
        return position >= 0 && position < this.count(addonClass);
    }
}
