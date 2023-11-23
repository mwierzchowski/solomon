package solomon2.core;

import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Context<C> implements Castable {
    protected Map<Object, Object> data;

    public abstract C getCommand();

    public void store(@NonNull Object key, Object object) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, object);
    }

    public Object retrieve(Object key) {
        if (this.data == null) {
            return null;
        }
        return this.data.get(key);
    }

    public <T> T retrieve(Object key, Class<T> clazz) {
        return clazz.cast(this.retrieve(key));
    }

    public boolean contains(Object key) {
        return this.retrieve(key) != null;
    }
}
