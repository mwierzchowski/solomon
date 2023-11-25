package solomon2.core.configs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import solomon2.core.Addon;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyConfig implements Config {
    public static final EmptyConfig INSTANCE = new EmptyConfig();

    @Override
    public <T extends Addon> T get(Class<T> clazz, int index) {
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public <T extends Addon> Config add(Class<T> clazz, T item) {
        return new ListConfig().add(clazz, item);
    }

    @Override
    public int size(Class<? extends Addon> clazz) {
        return 0;
    }

    @Override
    public Config chain() {
        return this;
    }
}
