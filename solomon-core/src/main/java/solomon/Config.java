package solomon;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.addons.Addon;
import solomon.addons.Decorator;
import solomon.addons.Listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static solomon.Utils.cast;

@Slf4j
@NoArgsConstructor
public class Config {
    private boolean locked = false;
    private Config parent;
    private List<Decorator<?, ?>> decoratorList;
    private List<Listener<?, ?>> listenerList;

    public Config(List<Addon> addons) {
        addons.forEach(this::add);
    }

    public Config(Config parent) {
        this.parent = parent;
    }

    public void lock() {
        this.locked = true;
    }

    public Config unlock() {
        if (locked) {
            return new Config(this);
        } else {
            return this;
        }
    }

    public void add(Addon addon) {
        if (locked) {
            throw new IllegalStateException("Cannot modify locked config");
        }
        LOG.debug("Adding addon: {}", addon);
        Class<? extends Addon> addonClass;
        if (addon instanceof Decorator) {
            addonClass = Decorator.class;
        } else if (addon instanceof Listener) {
            addonClass = Listener.class;
        } else {
            // this will not work but at least method accessing list can throw exception with meaningful message
            addonClass = addon.getClass();
        }
        addonList(addonClass, true).add(addon);
    }

    public <A extends Addon> A get(Class<A> addonClass, int position) {
        LOG.debug("Getting {} on position {}", addonClass, position);
        var parentSize = this.parentSize(addonClass);
        if (position < 0) {
            throw new IndexOutOfBoundsException(position);
        } else if (position < parentSize) {
            return this.parent.get(addonClass, position);
        } else {
            var addon = this.addonList(addonClass, false).get(position - parentSize);
            return cast(addon);
        }
    }

    public int count(Class<? extends Addon> addonClass) {
        return this.parentSize(addonClass) + addonList(addonClass, false).size();
    }

    boolean contains(Class<? extends Addon> addonClass, int position) {
        return position >= 0 && position < this.count(addonClass);
    }

    protected int parentSize(Class<? extends Addon> clazz) {
        return this.parent == null ? 0 : parent.count(clazz);
    }

    protected List<Addon> addonList(Class<? extends Addon> addonClass, boolean create) {
        List<? extends Addon> list;
        if (addonClass == Decorator.class) {
            if (this.decoratorList == null && create) {
                this.decoratorList = new ArrayList<>();
            }
            list = this.decoratorList;
        } else if (addonClass == Listener.class) {
            if (this.listenerList == null && create) {
                this.listenerList = new ArrayList<>();
            }
            list = this.listenerList;
        } else {
            var message = MessageFormat.format("Class {0} is not supported addon", addonClass);
            throw new IllegalArgumentException(message);
        }
        return list == null ?  emptyList() : cast(list);
    }
}