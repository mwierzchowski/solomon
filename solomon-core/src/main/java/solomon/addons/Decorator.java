package solomon.addons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solomon.Context;
import solomon.Result;

public interface Decorator<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Decorator.class);

    void before(Context<C> context);
    void after(Context<C> context, Result<V> result);

    default void safeBefore(Context<C> context, Result<V> result) {
        try {
            this.before(context);
        } catch (RuntimeException ex) {
            LOG.debug("Exception in decorator: {}", ex.getMessage());
            result.setException(ex);
        }
    }

    default void safeAfter(Context<C> context, Result<V> result) {
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
