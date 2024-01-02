package solomonx.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Decorator<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Decorator.class);

    default void safeBefore(Context<C> context, OutputStore<V> outputStore) {
        try {
            this.before(context);
        } catch (RuntimeException ex) {
            LOG.debug("Exception in decorator: {}", ex.getMessage());
            outputStore.setException(ex);
        }
    }

    default void safeAfter(Context<C> context, OutputStore<V> outputStore) {
        try {
            this.after(context, outputStore);
        } catch (RuntimeException ex) {
            if (outputStore.isSuccess()) {
                LOG.error("Could not execute after callback, marking command as failed");
                outputStore.setException(ex);
            } else {
                LOG.error("Could not execute after callback, command is already failed, this exception is lost", ex);
            }
        }
    }

    void before(Context<C> context);
    void after(Context<C> context, OutputStore<V> outputStore);
}
