package solomon.decorators;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import solomon.Decorator;
import solomon.Result;

import static org.slf4j.event.Level.DEBUG;
import static org.slf4j.event.Level.ERROR;

@Slf4j
@Setter
public class LoggingDecorator implements Decorator<Object, Object> {
    private Level startLevel = DEBUG;
    private Level successLevel = DEBUG;
    private Level failureLevel = ERROR;

    @Override
    public void before(Object command) {
        LOG.atLevel(startLevel).log("Executing command: {}", command);
    }

    @Override
    public void after(Object command, Result<Object> result) {
        if (result.isSuccess()) {
            LOG.atLevel(successLevel).log("Command finished in {} ms", result.getDuration());
        } else if(result.isDecorationFailure()) {
            LOG.atLevel(failureLevel).log("Decoration failed");
        } else {
            LOG.atLevel(failureLevel).log("Command failed in {} ms", result.getDuration());
        }
    }
}
