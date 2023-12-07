package solomon.annotations;

import solomon.addons.Decorator;
import solomon.addons.Observer;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
public @interface Command {
    Class<? extends Decorator>[] decorators() default {};
    Class<? extends Observer>[] observers() default {};
}
