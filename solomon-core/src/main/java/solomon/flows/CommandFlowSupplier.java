package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class CommandFlowSupplier<C extends Supplier<R>, R> extends CommandFlowBase<CommandFlowSupplier<C, R>, C, Supplier<R>, R> {
    protected R defaultResult;

    public CommandFlowSupplier(C command) {
        super(command);
    }

    public CommandFlowSupplier<C, R> decorateInline(@NonNull Function<Supplier<R>, R> decoratorLambda) {
        Supplier<R> prevCallStack = super.callStack;
        super.callStack = () -> decoratorLambda.apply(prevCallStack);
        LOG.trace("Command {} decorated by inline decorator", super.cmdName());
        return this;
    }

    public CommandFlowSupplier<C, R> defaultResult(@NonNull R result) {
        if (this.defaultResult != null) {
            LOG.warn("Command {} already has default result: {}", super.cmdName(), this.defaultResult);
        }
        this.defaultResult = result;
        return this;
    }

    public R execute() {
        R result;
        LOG.debug("Executing command: {}", super.command);
        try {
            long start = System.currentTimeMillis();
            result = super.callStack.get();
            long stop = System.currentTimeMillis();
            LOG.debug("Command {} successfully finished in {} ms and returned: {}", super.cmdName(), stop - start, result);
        } catch (RuntimeException runtimeException) {
            super.notifyOnFailure(runtimeException);
            if (this.defaultResult != null) {
                result = this.defaultResult;
                LOG.warn("Command {} failed - finishing with default result: {}", super.cmdName(), this.defaultResult, runtimeException);
            } else {
                throw runtimeException;
            }
        }
        super.notifyOnSuccess(result);
        return result;
    }

    public <T> T execute(@NonNull Function<R, T> resultMapper) {
        R result = execute();
        return resultMapper.apply(result);
    }
}
