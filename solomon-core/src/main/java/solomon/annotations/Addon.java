package solomon.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static solomon.annotations.Addon.CacheMode.DEFAULT;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
public @interface Addon {
    String cacheMode() default DEFAULT;

    interface CacheMode {
        String NONE = "none";
        String DEFAULT = "default";
    }
}
