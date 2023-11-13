package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Flow;
import solomon.decorators.DefaultValueDecorator;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class SupplierFlow<C extends Supplier<V>, V> extends Flow<SupplierFlow<C, V>, C, V> {
    public SupplierFlow(@NonNull C command, List<Decorator<Object, Object>> globalDecorators) {
        super(command, globalDecorators);
    }

    public SupplierFlow<C, V> defaultValue(V value) {
        super.findOrCreate(DefaultValueDecorator.class, DefaultValueDecorator::new)
                .setDefaultValue(value);
        return this;
    }

    public SupplierFlow<C, V> defaultValue(@NonNull Supplier<V> valueSupplier) {
        super.findOrCreate(DefaultValueDecorator.class, DefaultValueDecorator::new)
                .setDefaultValue(valueSupplier);
        return this;
    }

    @Override
    protected V internalExecute() {
        return super.command.get();
    }
}
