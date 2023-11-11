package solomon.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.CommandDecorator;
import solomon.CommandResult;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class DefaultValueDecorator implements CommandDecorator {
    @NonNull private final Supplier<Object> valueSupplier;

    public DefaultValueDecorator(@NonNull Object defaultValue) {
        this.valueSupplier = () -> defaultValue;
    }

    @Override
    public void after(Object command, CommandResult result) {
        if (result.isFailure()) {
            var defaultValue = valueSupplier.get();
            result.overrideValue(defaultValue);
            LOG.debug("Overriding command failure with default value: {}", defaultValue);
        }
    }
}
