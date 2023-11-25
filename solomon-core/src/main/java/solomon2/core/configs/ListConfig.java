package solomon2.core.configs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import solomon2.core.Addon;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@Setter
public class ListConfig implements Config {
    private List<Decorator<?, ?>> decorators;
    private List<Listener<?, ?>> listeners;

    @Override
    public <T extends Addon> T get(Class<T> clazz, int index) {
        return getList(clazz, false).get(index);
    }

    @Override
    public <T extends Addon> Config add(Class<T> clazz, T item) {
        LOG.debug("Adding {}: {}", clazz, item);
        getList(clazz, true).add(item);
        return this;
    }

    @Override
    public int size(Class<? extends Addon> clazz) {
        return getList(clazz, false).size();
    }

    @Override
    public Config chain() {
        return new LinkedConfig(this);
    }

    @SuppressWarnings("unchecked")
    protected <T extends Addon> List<T> getList(Class<T> clazz, boolean create) {
        List<?> list;
        if (clazz == Decorator.class) {
            if (this.decorators == null && create) {
                this.decorators = new ArrayList<>();
            }
            list = this.decorators;
        } else if (clazz == Listener.class) {
            if (this.listeners == null && create) {
                this.listeners = new ArrayList<>();
            }
            list = this.listeners;
        } else {
            var message = MessageFormat.format("Class {0} is not supported", clazz);
            throw new IllegalArgumentException(message);
        }
        return list != null ? (List<T>) list : emptyList();
    }
}
