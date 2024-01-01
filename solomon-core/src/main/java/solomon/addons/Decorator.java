package solomon.addons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solomon.ExecutionContext;
import solomon.MutableResult;

public interface Decorator<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Decorator.class);

    void before(ExecutionContext<C> context);
    void after(ExecutionContext<C> context, MutableResult<V> result);

    default void safeBefore(ExecutionContext<C> context, MutableResult<V> result) {
        try {
            this.before(context);
        } catch (RuntimeException ex) {
            LOG.debug("Exception in decorator: {}", ex.getMessage());
            result.setException(ex);
        }
    }

    default void safeAfter(ExecutionContext<C> context, MutableResult<V> result) {
        try {
            this.after(context, result);
        } catch (RuntimeException ex) {
            if (result.isSuccess()) {
                LOG.error("Could not execute after callback, marking command as failed");
                result.setException(ex);
            } else {
                LOG.error("Could not execute after callback, command is already failed, this exception is lost", ex);
            }
        }
    }
}
