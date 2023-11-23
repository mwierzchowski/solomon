package solomon2.tmp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyConfig implements  Config {
    public static final EmptyConfig INSTANCE = new EmptyConfig();

    @Override
    public <T> T get(ItemType type, int index) {
        throw new IndexOutOfBoundsException(index);
    }

    @Override
    public Config add(ItemType type, Object item) {
        return new FirstConfig().add(type, item);
    }

    @Override
    public int size(ItemType type) {
        return 0;
    }

    @Override
    public Config chain() {
        return this;
    }
}
