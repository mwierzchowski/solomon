package solomon.decorators;

import lombok.extern.slf4j.Slf4j;
import solomon.CommandDecorator;
import solomon.CommandResult;

import static java.text.MessageFormat.format;

@Slf4j
public class LoggingDecorator implements CommandDecorator {
    @Override
    public void before(Object command) {
        if (LOG.isDebugEnabled()) {
            var message = format("Executing command: {0}", command);
            LOG.debug(message);
        }
    }

    @Override
    public void after(Object command, CommandResult result) {
        if (result.isSuccess() && LOG.isDebugEnabled()) {
            var message = format("Command finished in {0} ms", result.getDuration());
            LOG.debug(message);
        } else if(result.isFailure() && LOG.isErrorEnabled()) {
            var message = format("Command failed in {0} ms", result.getDuration());
            LOG.error(message, (Throwable) result.getException());
        }
    }
}
