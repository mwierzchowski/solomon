package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Flow;
import solomon.decorators.DefaultValueDecorator;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class SupplierFlow<C extends Supplier<R>, R> extends Flow<SupplierFlow<C, R>, C, R> {
    public SupplierFlow(@NonNull C command, List<Decorator<Object, Object>> globalDecorators) {
        super(command, globalDecorators);
    }

    public SupplierFlow<C, R> defaultResult(R result) {
        super.findOrCreate(DefaultValueDecorator.class, DefaultValueDecorator::new)
                .setDefaultValue(result);
        return this;
    }

    public SupplierFlow<C, R> defaultResult(@NonNull Supplier<R> resultSupplier) {
        super.findOrCreate(DefaultValueDecorator.class, DefaultValueDecorator::new)
                .setDefaultValue(resultSupplier);
        return this;
    }

    @Override
    protected R internalExecute() {
        return super.command.get();
    }
}
