package solomon2.core.configs;

import lombok.AllArgsConstructor;
import solomon2.spi.Addon;

@AllArgsConstructor
public class LinkedConfig extends ListConfig {
    private Config previous;

    @Override
    public <A extends Addon> A get(Class<A> addonClass, int position) {
        var previousSize = this.previousSize(addonClass);
        if (position < previousSize) {
            return this.previous.get(addonClass, position);
        } else {
            return super.get(addonClass, position - previousSize);
        }
    }

    @Override
    public int count(Class<? extends Addon> addonClass) {
        return this.previousSize(addonClass) + super.count(addonClass);
    }

    protected int previousSize(Class<? extends Addon> clazz) {
        return this.previous == null ? 0 : previous.count(clazz);
    }
}
