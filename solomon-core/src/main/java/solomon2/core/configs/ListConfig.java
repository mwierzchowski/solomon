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
    public static final List<Class<? extends Addon>> SUPPORTED_ADDONS = asList(Decorator.class, Listener.class);

    private List<Decorator<?, ?>> decorators;
    private List<Listener<?, ?>> listeners;

    @Override
    public <A extends Addon> A getAddon(Class<A> addonClass, int position) {
        LOG.debug("Getting {} on position {}", addonClass, position);
        var addon = getList(addonClass, false).get(position);
        return cast(addon);
    }

    @Override
    public Config addAddon(Addon addon) {
        LOG.debug("Adding addon: {}", addon);
        getList(typeOf(addon), true).add(addon);
        return this;
    }

    @Override
    public int count(Class<? extends Addon> addonClass) {
        return getList(addonClass, false).size();
    }

    @Override
    public Config chain() {
        return new LinkedConfig(this);
    }

    protected Class<? extends Addon> typeOf(Addon addon) {
        var addonClass = addon.getClass();
        for (int i = 0; i < SUPPORTED_ADDONS.size(); i++) {
            var supportedClass = SUPPORTED_ADDONS.get(i);
            if (supportedClass.isAssignableFrom(addonClass)) {
                return supportedClass;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected List<Addon> getList(Class<? extends Addon> addonClass, boolean create) {
        List<?> list;
        if (addonClass == Decorator.class) {
            if (this.decorators == null && create) {
                this.decorators = new ArrayList<>();
            }
            list = this.decorators;
        } else if (addonClass == Listener.class) {
            if (this.listeners == null && create) {
                this.listeners = new ArrayList<>();
            }
            list = this.listeners;
        } else {
            var message = MessageFormat.format("Class {0} is not supported addon", addonClass);
            throw new IllegalArgumentException(message);
        }
        return list != null ? (List<Addon>) list : emptyList();
    }
}
