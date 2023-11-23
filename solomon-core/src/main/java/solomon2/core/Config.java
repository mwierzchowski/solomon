package solomon2.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class Config {
    private List<Decorator<Object, Object>> decorators;
    private List<Listener<Object, Object>> listeners;

    public Config addDecorator(@NonNull Decorator<?, ?> decorator) {
        LOG.debug("Adding decorator: {}", decorator);
        if (this.decorators == null) {
            this.decorators = new ArrayList<>();
        }
        this.decorators.add(decorator.cast());
        return this;
    }

    public Config addListener(@NonNull Listener<?, ?> listener) {
        LOG.debug("Adding listener: {}", listener);
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }
        this.listeners.add(listener.cast());
        return this;
    }
}
