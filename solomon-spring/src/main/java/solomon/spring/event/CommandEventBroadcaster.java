package solomon.spring.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import solomon.addons.Observer;
import solomon.spring.annotation.Global;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "solomon.event-broadcaster.enabled", matchIfMissing = true)
@Global(onProperty = "solomon.event-broadcaster.global")
public class CommandEventBroadcaster implements Observer<Object, Object> {
    private final ApplicationContext applicationContext;

    @Override
    public void onSuccess(Object command, Object value) {
        LOG.debug("Broadcasting success event");
        var event = new CommandSuccessEvent(command, value);
        applicationContext.publishEvent(event);
    }

    @Override
    public void onFailure(Object command, RuntimeException exception) {
        LOG.debug("Broadcasting failure event");
        var event = new CommandFailureEvent(command, exception);
        applicationContext.publishEvent(event);
    }
}
