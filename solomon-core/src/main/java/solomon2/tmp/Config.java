package solomon2.tmp;

public interface Config {
    enum ItemType {
        Decorator, Listener
    }

    <T> T get(ItemType type, int index);
    Config add(ItemType type, Object item);
    int size(ItemType type);
    Config chain();

    default boolean contains(ItemType type, int index) {
        return index >= 0 && index < this.size(type);
    }

    static Config emptyConfig() {
        return EmptyConfig.INSTANCE;
    }
}
