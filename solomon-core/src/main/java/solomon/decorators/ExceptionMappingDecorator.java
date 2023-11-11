package solomon.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.CommandDecorator;
import solomon.CommandResult;

@Slf4j
@RequiredArgsConstructor
public class ExceptionMappingDecorator implements CommandDecorator {
    @NonNull private final Class<? extends Exception> sourceClass;
    @NonNull private final Class<? extends Exception> targetClass;

    @Override
    public void after(Object command, CommandResult result) {
        Exception source = result.getException();
        if (result.isFailure() && source.getClass().isAssignableFrom(sourceClass)) {
            Exception target = mapExceptionFrom(source);
            result.overrideException(target);
            LOG.debug("Mapped command exception from {} to {}", sourceClass, targetClass);
        }
    }

    private Exception mapExceptionFrom(Exception source) {
        try {
            return targetClass.getConstructor(sourceClass).newInstance(source);
        } catch (Exception ex) {
            LOG.error("Could not map command exception", ex);
            return source;
        }
    }
}
