package solomon.spring.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import solomon.addons.Observer;
import solomon.spring.annotation.Global;

@Slf4j
@Global
@RequiredArgsConstructor
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
