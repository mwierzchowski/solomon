package solomon.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Result;

@Slf4j
@RequiredArgsConstructor
public class ExceptionMappingDecorator implements Decorator {
    @NonNull private final Class<? extends RuntimeException> sourceClass;
    @NonNull private final Class<? extends RuntimeException> targetClass;

    @Override
    public void after(Object command, Result result) {
        var source = result.getException();
        if (result.isFailure() && source.getClass().isAssignableFrom(sourceClass)) {
            var target = mapExceptionFrom(source);
            result.overrideException(target);
            LOG.debug("Mapped command exception from {} to {}", sourceClass, targetClass);
        }
    }

    private RuntimeException mapExceptionFrom(RuntimeException source) {
        try {
            return targetClass.getConstructor(sourceClass).newInstance(source);
        } catch (Exception ex) {
            LOG.error("Could not map command exception", ex);
            return source;
        }
    }
}
