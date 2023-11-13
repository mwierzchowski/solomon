package solomon.decorators;

import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Result;

@Slf4j
public class LoggingDecorator implements Decorator<Object> {
    @Override
    public void before(Object command) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing command: {}", command);
        }
    }

    @Override
    public void after(Object command, Result result) {
        if (result.isSuccess() && LOG.isDebugEnabled()) {
            LOG.debug("Command finished in {} ms", result.getDuration());
        } else if(result.isFailure() && LOG.isErrorEnabled()) {
            LOG.error("Command failed in {} ms", result.getDuration());
        }
    }
}
