package solomon2.core;

import lombok.NonNull;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigSet implements Customization {
    private static final int GLOBAL_INDEX = 0;
    private static final int CUSTOM_INDEX = 1;

    private final List[] decoratorsArray = new List[2];
    private final List[] listenersArray = new List[2];

    public ConfigSet(Config globalConfig) {
        if (globalConfig != null) {
            this.decoratorsArray[GLOBAL_INDEX] = globalConfig.getDecorators();
            this.listenersArray[GLOBAL_INDEX] = globalConfig.getListeners();
        }
    }

    public ConfigSet ifEmpty(Config globalConfig) {
        if (globalConfig != null) {
            if (this.decoratorsArray[GLOBAL_INDEX] == null) {
                this.decoratorsArray[GLOBAL_INDEX] = globalConfig.getDecorators();
            }
            if (this.listenersArray[GLOBAL_INDEX] == null) {
                this.listenersArray[GLOBAL_INDEX] = globalConfig.getListeners();
            }
        }
        return this;
    }

    @Override
    public Customization addDecorator(@NonNull Decorator<?, ?> decorator) {
        if (this.decoratorsArray[CUSTOM_INDEX] == null) {
            this.decoratorsArray[CUSTOM_INDEX] = new ArrayList<>();
        }
        this.decoratorsArray[CUSTOM_INDEX].add(decorator);
        return this;
    }

    @Override
    public Customization addListener(@NonNull Listener<?, ?> listener) {
        if (this.listenersArray[CUSTOM_INDEX] == null) {
            this.listenersArray[CUSTOM_INDEX] = new ArrayList<>();
        }
        this.listenersArray[CUSTOM_INDEX].add(listener);
        return this;
    }

    public Iterable<Decorator<Object, Object>> decorators() {
        // TODO
        if (this.decoratorsArray[CUSTOM_INDEX] == null) {
            return Collections.emptyList();
        } else {
            return this.decoratorsArray[CUSTOM_INDEX];
        }
    }

    public Iterable<Decorator<Object, Object>> decoratorsReversed(int count) {
        // TODO
        if (this.decoratorsArray[CUSTOM_INDEX] == null) {
            return Collections.emptyList();
        } else {
            return this.decoratorsArray[CUSTOM_INDEX];
        }
    }

    public Iterable<Listener<Object, Object>> listeners() {
        // TODO
        if (this.listenersArray[CUSTOM_INDEX] == null) {
            return Collections.emptyList();
        } else {
            return this.listenersArray[CUSTOM_INDEX];
        }
    }
}
