package solomon.spring.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import solomon.addons.Decorator;
import solomon.addons.Listener;
import solomon.annotations.CommandConfig;

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
@CommandConfig
public @interface CommandBean {
    @AliasFor(annotation = CommandConfig.class, attribute = "decorators")
    Class<? extends Decorator>[] decorators() default {};

    @AliasFor(annotation = CommandConfig.class, attribute = "listeners")
    Class<? extends Listener>[] listeners() default {};
}
