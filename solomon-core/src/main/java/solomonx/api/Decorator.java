package solomonx.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Decorator<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Decorator.class);

    default void safeBefore(Context<C, V> context) {
        try {
            this.before(context);
        } catch (RuntimeException ex) {
            LOG.debug("Exception in decorator: {}", ex.getMessage());
            context.setException(ex);
        }
    }

    default void safeAfter(Context<C, V> context) {
        try {
            this.after(context);
        } catch (RuntimeException ex) {
            if (context.isSuccess()) {
                LOG.error("Could not execute after callback, marking command as failed");
                context.setException(ex);
            } else {
                LOG.error("Could not execute after callback, command is already failed, this exception will be lost", ex);
            }
        }
    }

    void before(Context<C, V> context);
    void after(Context<C, V> context);
}
