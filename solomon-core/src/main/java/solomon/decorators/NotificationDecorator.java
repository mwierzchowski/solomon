package solomon.decorators;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.CommandDecorator;
import solomon.CommandResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Collections.addAll;

@Slf4j
public class NotificationDecorator implements CommandDecorator {
    protected List<Consumer<Object>> successListeners;
    protected List<Consumer<Exception>> failureListeners;

    @SafeVarargs
    public final void addSuccessListeners(@NonNull final Consumer<Object>... listeners) {
        if (this.successListeners == null) {
            this.successListeners = new ArrayList<>();
        }
        addAll(this.successListeners, listeners);
    }

    @SafeVarargs
    public final void addFailureListeners(@NonNull final Consumer<Exception>... listeners) {
        if (this.failureListeners == null) {
            this.failureListeners = new ArrayList<>();
        }
        addAll(this.failureListeners, listeners);
    }

    @Override
    public void after(Object command, CommandResult result) {
        int counter = 0;
        if (result.isSuccess() && this.successListeners != null) {
            counter = successListeners.stream()
                    .mapToInt(listener -> sendSafeNotification(listener, result.getValue()))
                    .sum();
        } else if (result.isFailure() && this.failureListeners != null) {
            counter = failureListeners.stream()
                    .mapToInt(listener -> sendSafeNotification(listener, result.getException()))
                    .sum();
        }
        LOG.debug("Sent {} notifications", counter);
    }

    protected <C> int sendSafeNotification(Consumer<C> listener, C content) {
        try {
            listener.accept(content);
            return 1;
        } catch (Exception ex) {
            LOG.error("Command failed sending notification", ex);
            return 0;
        }
    }
}
