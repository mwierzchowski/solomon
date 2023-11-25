package solomon2.core.configs;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NextConfig extends FirstConfig {
    private Config previous;

    @Override
    public <T> T get(Class<T> clazz, int index) {
        var previousSize = this.previousSize(clazz);
        if (index < previousSize) {
            return this.previous.get(clazz, index);
        } else {
            return super.get(clazz, index - previousSize);
        }
    }

    @Override
    public int size(Class<?> clazz) {
        return this.previousSize(clazz) + super.size(clazz);
    }

    protected int previousSize(Class<?> clazz) {
        return this.previous == null ? 0 : previous.size(clazz);
    }
}
