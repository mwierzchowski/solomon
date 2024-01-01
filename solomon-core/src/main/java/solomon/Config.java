package solomon;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.addons.Addon;
import solomon.addons.Decorator;
import solomon.addons.Observer;

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
    private List<Observer<?, ?>> observerList;

    public Config(Config parent) {
        this.parent = parent;
    }

    public void lock() {
        this.locked = true;
        LOG.debug("Configuration locked");
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
        } else if (addon instanceof Observer) {
            addonClass = Observer.class;
        } else {
            // this will not work but at least method accessing list can throw exception with meaningful message
            addonClass = addon.getClass();
        }
        addonList(addonClass, true).add(addon);
    }

    public void addAll(List<Addon> addons) {
        addons.forEach(this::add);
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
        } else if (addonClass == Observer.class) {
            if (this.observerList == null && create) {
                this.observerList = new ArrayList<>();
            }
            list = this.observerList;
        } else {
            var message = MessageFormat.format("Class {0} is not supported addon", addonClass);
            throw new IllegalArgumentException(message);
        }
        return list == null ?  emptyList() : cast(list);
    }
}