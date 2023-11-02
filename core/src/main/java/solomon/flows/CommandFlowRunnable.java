package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class CommandFlowRunnable<C extends Runnable> extends CommandFlowBase<CommandFlowRunnable<C>, C, Runnable, Void> {
    public CommandFlowRunnable(C command) {
        super(command);
    }

    public CommandFlowRunnable<C> decorateInline(@NonNull Consumer<Runnable> decoratorLambda) {
        Runnable prevCallStack = super.callStack;
        super.callStack = () -> decoratorLambda.accept(prevCallStack);
        log.trace("Command {} decorated by inline decorator", super.cmdName());
        return this;
    }

    public void execute() {
        log.debug("Executing command: {}", super.command);
        try {
            long start = System.currentTimeMillis();
            super.callStack.run();
            long stop = System.currentTimeMillis();
            log.debug("Command {} successfully finished in {} ms", super.cmdName(), stop - start);
        } catch (RuntimeException runtimeException) {
            super.notifyOnFailure(runtimeException);
            throw runtimeException;
        }
        super.notifyOnSuccess(null);
    }
}
