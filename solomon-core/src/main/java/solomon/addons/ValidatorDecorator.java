package solomon.addons;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import solomon.ExecutionContext;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class ValidatorDecorator extends DecoratorAdapter<Object, Object> {
    private final Validator validator;

    @Override
    public void before(ExecutionContext<Object> context) {
        var command = context.getCommand();
        Set<ConstraintViolation<Object>> violations = validator.validate(command);
        if (violations.isEmpty()) {
            LOG.debug("Command {} was positively validated", command.getClass());
        } else {
            LOG.debug("Command {} failed validation, {} violation(s) were found", command.getClass(), violations.size());
            throw new ConstraintViolationException(violations);
        }
    }
}
