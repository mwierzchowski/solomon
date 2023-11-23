package solomon2.tmp;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NextConfig extends FirstConfig {
    private Config previous;

    @Override
    public <T> T get(ItemType type, int index) {
        var previousSize = this.previousSize(type);
        if (index < previousSize) {
            return this.previous.get(type, index);
        } else {
            return super.get(type, index - previousSize);
        }
    }

    @Override
    public int size(ItemType type) {
        return this.previousSize(type) + super.size(type);
    }

    protected int previousSize(ItemType type) {
        return this.previous == null ? 0 : previous.size(type);
    }
}
