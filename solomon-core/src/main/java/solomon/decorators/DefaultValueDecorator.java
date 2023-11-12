package solomon.decorators;

import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Result;

import java.util.function.Supplier;

@Slf4j
public class DefaultValueDecorator implements Decorator<Object> {
    private Supplier<Object> defaultValueSupplier;

    public void setDefaultValue(Object defaultValue) {
        this.defaultValueSupplier = () -> defaultValue;
    }

    public void setDefaultValue(Supplier<Object> defaultValueSupplier) {
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public void after(Object command, Result result) {
        if (result.isFailure()) {
            var defaultValue = defaultValueSupplier != null ? defaultValueSupplier.get() : null;
            result.overrideValue(defaultValue);
            LOG.debug("Overriding command failure with default value: {}", defaultValue);
        }
    }
}
