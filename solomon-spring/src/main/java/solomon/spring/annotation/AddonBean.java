package solomon.spring.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;
import solomon.annotations.AddonConfig;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

@Retention(RUNTIME)
@Target(TYPE)
@Documented
@Inherited
@Component
@Scope
@AddonConfig
public @interface AddonBean {
    @AliasFor(annotation = Scope.class, attribute = "scopeName")
    String scope() default SCOPE_SINGLETON;
}
