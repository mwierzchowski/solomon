package solomon2.core.configs;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import solomon2.spi.Addon;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static solomon2.core.Utils.cast;

@Slf4j
@Setter
public class ListConfig implements Config {
    protected static final List<Class<? extends Addon>> SUPPORTED_ADDON_CLASSES = asList(Decorator.class, Listener.class);

    private List<Decorator<?, ?>> decoratorList;
    private List<Listener<?, ?>> listenerList;

    @Override
    public <A extends Addon> A getAddon(Class<A> addonClass, int position) {
        LOG.debug("Getting {} on position {}", addonClass, position);
        var addon = addonList(addonClass, false).get(position);
        return cast(addon);
    }

    @Override
    public Config addAddon(Addon addon) {
        LOG.debug("Adding addon: {}", addon);
        addonList(typeOf(addon), true).add(addon);
        return this;
    }

    @Override
    public int count(Class<? extends Addon> addonClass) {
        return addonList(addonClass, false).size();
    }

    @Override
    public Config chain() {
        return new LinkedConfig(this);
    }

    protected Class<? extends Addon> typeOf(Addon addon) {
        var addonClass = addon.getClass();
        for (int i = 0; i < SUPPORTED_ADDON_CLASSES.size(); i++) {
            var supportedClass = SUPPORTED_ADDON_CLASSES.get(i);
            if (supportedClass.isAssignableFrom(addonClass)) {
                return supportedClass;
            }
        }
        return null;
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
