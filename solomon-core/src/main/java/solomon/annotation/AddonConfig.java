package solomon.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static solomon.annotation.AddonConfig.Scope.SERVICE;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
public @interface AddonConfig {
    enum Scope {
        SERVICE, PROTOTYPE
    }

    Scope scope() default SERVICE;
}
