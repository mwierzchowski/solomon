package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Flow;
import solomon.decorators.DefaultValueDecorator;

import java.util.List;
import java.util.function.Supplier;

@Slf4j
public class CommandFlowSupplier<C extends Supplier<R>, R> extends Flow<CommandFlowSupplier<C, R>, C, R> {
    public CommandFlowSupplier(@NonNull C command, List<Decorator> globalDecorators) {
        super(command, globalDecorators);
    }

    public CommandFlowSupplier<C, R> defaultResult(R result) {
        super.findOrCreate(DefaultValueDecorator.class, DefaultValueDecorator::new)
                .setDefaultValue(result);
        return this;
    }

    public CommandFlowSupplier<C, R> defaultResult(@NonNull Supplier<R> resultSupplier) {
        super.findOrCreate(DefaultValueDecorator.class, DefaultValueDecorator::new)
                .setDefaultValue(resultSupplier);
        return this;
    }

    @Override
    protected R internalExecute() {
        return super.command.get();
    }
}
