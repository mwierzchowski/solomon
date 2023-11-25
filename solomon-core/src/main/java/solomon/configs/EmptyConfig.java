package solomon.configs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import solomon.spi.Addon;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyConfig implements Config {
    public static final EmptyConfig INSTANCE = new EmptyConfig();

    @Override
    public Config add(Addon addon) {
        return new LinkedConfig(null).add(addon);
    }

    @Override
    public <A extends Addon> A get(Class<A> addonClass, int position) {
        throw new IndexOutOfBoundsException(position);
    }

    @Override
    public int count(Class<? extends Addon> addonClass) {
        return 0;
    }

    @Override
    public Config chain() {
        return this;
    }

    @Override
    public boolean contains(Class<? extends Addon> addonClass, int position) {
        return false;
    }
}
