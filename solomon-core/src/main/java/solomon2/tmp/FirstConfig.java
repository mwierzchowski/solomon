package solomon2.tmp;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon2.spi.Listener;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Slf4j
@Setter
public class FirstConfig implements Config {
    private List<Decorator<Object, Object>> decorators;
    private List<Listener<Object, Object>> listeners;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(ItemType type, int index) {
        return (T) getList(type, false).get(index);
    }

    @Override
    public Config add(ItemType type, Object item) {
        LOG.debug("Adding {}: {}", type, item);
        getList(type, true).add(item);
        return this;
    }

    @Override
    public int size(ItemType type) {
        return getList(type, false).size();
    }

    @Override
    public Config chain() {
        return new NextConfig(this);
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(ItemType type, boolean create) {
        var list = switch (type) {
            case Decorator -> {
                if (this.decorators == null && create) {
                    this.decorators = new ArrayList<>();
                }
                yield this.decorators;
            }
            case Listener -> {
                if (this.listeners == null && create) {
                    this.listeners = new ArrayList<>();
                }
                yield this.listeners;
            }
        };
        return list != null ? (List<T>) list : emptyList();
    }
}
