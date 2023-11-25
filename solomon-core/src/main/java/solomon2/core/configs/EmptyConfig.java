package solomon2.core.configs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import solomon2.core.Addon;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyConfig implements Config {
    public static final EmptyConfig INSTANCE = new EmptyConfig();

    @Override
    public <A extends Addon> A getAddon(Class<A> addonClass, int position) {
        throw new IndexOutOfBoundsException(position);
    }

    @Override
    public Config addAddon(Addon addon) {
        return new ListConfig().addAddon(addon);
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
