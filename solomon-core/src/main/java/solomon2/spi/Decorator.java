package solomon2.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solomon2.ExecutionResult;

public interface Decorator<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Decorator.class);

    void before(Context<C> context);
    void after(Context<C> context, ExecutionResult<V> result);

    default void safeAfter(Context<C> context, ExecutionResult<V> result) {
        try {
            this.after(context, result);
        } catch (RuntimeException ex) {
            if (result.changeIfSuccessful(ex)) {
                LOG.error("Could not execute after callback, marking command as failed");
            } else {
                LOG.error("Could not execute after callback, command is already failed", ex);
            }
        }
    }
}
