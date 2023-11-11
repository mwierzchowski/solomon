package solomon.decorators;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.CommandDecorator;
import solomon.CommandResult;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class NotificationDecorator implements CommandDecorator {
    private final List<Consumer<Object>> successListeners;
    private final List<Consumer<Exception>> failureListeners;

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
