package solomon.spring.adapter;

import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import solomon.addons.ValidatorDecorator;
import solomon.spring.annotation.Global;

@Component
@ConditionalOnClass(Validator.class)
@ConditionalOnProperty(name = "solomon.validator.enabled", matchIfMissing = true)
@Global(onProperty = "solomon.validator.global")
public class ValidatorAdapter extends ValidatorDecorator {
    public ValidatorAdapter(Validator validator) {
        super(validator);
    }
}
