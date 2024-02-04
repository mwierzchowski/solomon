package solomonx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static solomonx.annotation.Priority.REGULAR;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
public @interface Applied {
    Priority priority() default REGULAR;
}
