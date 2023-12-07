package solomon.spring.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static solomon.spring.annotation.AddonBean.Priority.REGULAR;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
@Component
@Scope
@Order
public @interface AddonBean {
    @AliasFor(annotation = Order.class, attribute = "value")
    int priority() default REGULAR;

    boolean useGlobally() default false;

    String useGloballyString() default "";

    interface Priority {
        int HIGHEST = 100;
        int HIGH = 200;
        int REGULAR = 300;
        int LOW = 400;
        int LOWEST = 500;
    }
}
