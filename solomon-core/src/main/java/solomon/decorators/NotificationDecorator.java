package solomon.decorators;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class NotificationDecorator<R> implements Decorator<Object> {
    private List<Consumer<R>> successListeners;
    private List<Consumer<RuntimeException>> failureListeners;

    @SuppressWarnings("unchecked")
    public void addSuccessListener(@NonNull Consumer<R> listener) {
        if (successListeners == null) {
            successListeners = new ArrayList<>();
        }
        successListeners.add(listener);
        LOG.debug("Added success listener: {}", listener);
    }

    public void addFailureListener(@NonNull Consumer<RuntimeException> listener) {
        if (failureListeners == null) {
            failureListeners = new ArrayList<>();
        }
        failureListeners.add(listener);
        LOG.debug("Added failure listener: {}", listener);
    }

    @Override
    public void after(Object command, Result result) {
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

    protected <T> int sendSafeNotification(Consumer<T> listener, T content) {
        try {
            listener.accept(content);
            return 1;
        } catch (Exception ex) {
            LOG.error("Notification failed", ex);
            return 0;
        }
    }
}
