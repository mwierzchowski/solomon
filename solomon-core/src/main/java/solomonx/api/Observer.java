package solomonx.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Observer<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Observer.class);

    default void safeOnSuccess(C command, V value) {
        try {
            this.onSuccess(command, value);
        } catch (RuntimeException ex) {
            LOG.error("Could not send success notification", ex);
        }
    }

    default  void safeOnFailure(C command, RuntimeException exception) {
        try {
            this.onFailure(command, exception);
        } catch (RuntimeException ex) {
            LOG.error("Could not send failure notification", ex);
        }
    }

    default void safeNotification(C command, OutputStore<V> outputStore) {
        if (outputStore.isSuccess()) {
            this.onSuccess(command, outputStore.getValue());
        } else {
            this.onFailure(command, outputStore.getException());
        }
    }

    void onSuccess(C command, V value);
    void onFailure(C command, RuntimeException exception);
}
