package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.PropertyResolver;
import solomon.Config;
import solomon.addons.Addon;
import solomon.spring.annotation.Global;

//@Slf4j TODO
@RequiredArgsConstructor
public class GlobalAddonBeanProcessor implements BeanPostProcessor {
    private final PropertyResolver propertyResolver;
    private final Config config;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Addon addon) {
            var globalAnnotation = getClass().getAnnotation(Global.class);
            if (globalAnnotation != null) {
                var globalFlag = globalAnnotation.value();
                var globalProperty = globalAnnotation.property();
                if (globalProperty != null && !globalProperty.isEmpty()) {
                    globalProperty = propertyResolver.resolvePlaceholders(globalProperty);
                    globalFlag = Boolean.parseBoolean(globalProperty);
                }
                if (globalFlag) {
                    config.add(addon);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
