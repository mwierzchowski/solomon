package solomon.spring.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import solomon.addons.Decorator;
import solomon.addons.Observer;
import solomon.annotations.Command;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
@Component
@Scope(SCOPE_PROTOTYPE)
@Command
public @interface CommandBean {
    @AliasFor(annotation = Command.class, attribute = "decorators")
    Class<? extends Decorator>[] decorators() default {};

    @AliasFor(annotation = Command.class, attribute = "observers")
    Class<? extends Observer>[] observers() default {};
}
