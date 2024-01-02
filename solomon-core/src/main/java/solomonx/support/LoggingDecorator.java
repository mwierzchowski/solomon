package solomonx.support;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import solomon.ExecutionContext;
import solomon.MutableResult;
import solomon.addons.Decorator;

import static org.slf4j.event.Level.DEBUG;
import static solomon.Utils.shortNameFor;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class LoggingDecorator implements Decorator<Object, Object> {
    private Level regularLevel = DEBUG;
    private Level failureLevel = DEBUG;
    private boolean includeDetails = true;

    @Override
    public void before(ExecutionContext<Object> context) {
        if (LOG.isEnabledForLevel(regularLevel)) {
            var message = includeDetails
                    ? "Executing command {}: {}"
                    : "Executing command {}";
            var command = context.getCommand();
            LOG.atLevel(regularLevel).log(message, shortNameFor(command), command);
        }
    }

    @Override
    public void after(ExecutionContext<Object> context, MutableResult<Object> result) {
        if (result.isSuccess()) {
            if (LOG.isEnabledForLevel(regularLevel)) {
                var message = includeDetails
                        ? "Command {} finished with success: {}"
                        : "Command {} finished with success";
                var command = context.getCommand();
                LOG.atLevel(regularLevel).log(message, shortNameFor(command), result.getValue());
            }
        } else {
            if (LOG.isEnabledForLevel(failureLevel)) {
                var command = context.getCommand();
                var exception = result.getException();
                LOG.atLevel(failureLevel).log("Command {} finished with {}: {}",
                        shortNameFor(command), shortNameFor(exception), exception.getMessage());
            }
        }
    }
}
