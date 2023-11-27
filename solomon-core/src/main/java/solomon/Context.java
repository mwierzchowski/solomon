package solomon;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

import static solomon.Utils.cast;

public interface Context<C> {
    Map<Object, Object> getContextData();
    void setContextData(Map<Object, Object> contextData);
    C getCommand();

    default void store(@NonNull Object key, Object object) {
        if (this.getContextData() == null) {
            this.setContextData(new HashMap<>());
        }
        getContextData().put(key, object);
    }

    default Object retrieve(Object key) {
        var data = this.getContextData();
        if (data == null) {
            return null;
        }
        return data.get(key);
    }

    default <T> T retrieve(Object key, Class<T> clazz) {
        return clazz.cast(this.retrieve(key));
    }

    default boolean contains(Object key) {
        return this.retrieve(key) != null;
    }

    default <T> Context<T> asContext() {
        return cast(this);
    }
}
