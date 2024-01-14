package solomonx.spi;

import lombok.Data;
import solomonx.api.Addon;
import solomonx.util.Cast;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.function.Consumer;

@Data
public class AddonData {
    private final Class<? extends Addon> key;
    private final Addon instance;
    private Collection<Annotation> annotations;
    private Boolean applied;
    private Integer priority;
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
        this.setApplied(true);
    }
}
