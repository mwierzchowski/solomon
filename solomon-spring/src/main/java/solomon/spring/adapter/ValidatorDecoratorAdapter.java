package solomon.spring.adapter;

import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import solomon.addons.ValidatorDecorator;
import solomon.spring.annotation.Addon;
import solomon.spring.annotation.Global;

@Addon
@ConditionalOnClass(Validator.class)
@ConditionalOnProperty(name = "solomon.validator.enabled", matchIfMissing = true)
@Global(onProperty = "solomon.validator.global")
public class ValidatorDecoratorAdapter extends ValidatorDecorator {
    public ValidatorDecoratorAdapter(Validator validator) {
        super(validator);
    }
}
