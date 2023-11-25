package solomon2.support;

import lombok.NonNull;
import solomon2.spi.Context;

import java.util.HashMap;
import java.util.Map;

public abstract class ContextAdapter<C> implements Context<C> {
    protected Map<Object, Object> data;

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
}
