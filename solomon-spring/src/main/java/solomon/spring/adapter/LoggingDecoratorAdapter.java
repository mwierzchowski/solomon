package solomon.spring.adapter;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import solomon.addons.LoggingDecorator;
import solomon.spring.annotation.Addon;
import solomon.spring.annotation.Global;

@Addon
@ConditionalOnProperty(name = "solomon.logging.enabled", matchIfMissing = true)
@Global(onProperty = "solomon.logging.global")
public class LoggingDecoratorAdapter extends LoggingDecorator {
    public LoggingDecoratorAdapter(@Value("${solomon.logging.level.regular:DEBUG}") Level regularLevel,
                                   @Value("${solomon.logging.level.failure:DEBUG}") Level failureLevel,
                                   @Value("${solomon.logging.details:true}") boolean includeDetails) {
        super(regularLevel, failureLevel, includeDetails);
    }
}
