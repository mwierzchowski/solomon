package solomon.spi;

import lombok.NonNull;

public interface Context<C> {
    C getCommand();
    void store(@NonNull Object key, Object object);
    Object retrieve(Object key);

    default <T> T retrieve(Object key, Class<T> clazz) {
        return clazz.cast(this.retrieve(key));
    }

    default boolean contains(Object key) {
        return this.retrieve(key) != null;
    }
}
