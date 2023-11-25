package solomon2.core.configs;

public interface Config {
    static Config emptyConfig() {
        return EmptyConfig.INSTANCE;
    }

    <T> T get(Class<T> clazz, int index);
    <T> Config add(Class<T> clazz, T item);
    int size(Class<?> clazz);
    Config chain();

    default boolean contains(Class<?> clazz, int index) {
        return index >= 0 && index < this.size(clazz);
    }
}
