package solomon.configs;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import solomon.spi.Addon;
import solomon.spi.Decorator;
import solomon.spi.Listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static solomon.Utils.cast;

@Slf4j
@RequiredArgsConstructor
public class LinkedConfig implements Config {
    private final LinkedConfig parent;

    @Setter
    private List<Decorator<?, ?>> decoratorList;

    @Setter
    private List<Listener<?, ?>> listenerList;

    @Override
    public Config add(Addon addon) {
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
        return this;
    }

    @Override
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

    @Override
    public int count(Class<? extends Addon> addonClass) {
        return this.parentSize(addonClass) + addonList(addonClass, false).size();
    }

    @Override
    public Config chain() {
        return new LinkedConfig(this);
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
