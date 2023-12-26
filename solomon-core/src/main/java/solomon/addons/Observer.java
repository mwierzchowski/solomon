package solomon.addons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import solomon.MutableResult;

public interface Observer<C, V> extends Addon {
    Logger LOG = LoggerFactory.getLogger(Observer.class);

    void onSuccess(C command, V value);

    void onFailure(C command, RuntimeException exception);

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

    default void safeNotification(C command, MutableResult<V> result) {
        if (result.isSuccess()) {
            this.onSuccess(command, result.getValue());
        } else {
            this.onFailure(command, result.getException());
        }
    }
}
