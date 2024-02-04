package solomonx.spi;

import lombok.Data;
import solomonx.annotation.Priority;
import solomonx.api.Addon;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Consumer;

import static solomonx.annotation.Priority.REGULAR;

@Data
public class AddonData {
    private final Class<? extends Addon> key;
    private final Addon instance;
    private Collection<Annotation> annotations;
    private Boolean applied;
    private Priority priority;
    private Boolean disabled;

    public AddonData(Addon instance) {
        this.key = instance.getClass();
        this.instance = instance;
    }

    public AddonData customize(Consumer<AddonData> customization) {
        customization.accept(this);
        return this;
    }

    public void apply() {
        this.applied = true;
        this.disabled = false;
        this.priority = REGULAR;
    }

    public void disable() {
        this.applied = false;
        this.disabled = true;
        this.priority = null;
    }
}
