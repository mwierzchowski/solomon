package solomon.spring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.PropertyResolver;
import solomon.Config;
import solomon.addons.Addon;
import solomon.spring.annotation.Global;

@Slf4j
@RequiredArgsConstructor
public class GlobalAddonBeanProcessor implements BeanPostProcessor {
    private final PropertyResolver propertyResolver;
    private final Config config;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Addon addon) {
            var globalAnnotation = bean.getClass().getAnnotation(Global.class);
            if (globalAnnotation != null) {
                LOG.debug("Processing addon {}", beanName);
                var globalFlag = globalAnnotation.value();
                var globalProperty = globalAnnotation.property();
                if (globalProperty != null && !globalProperty.isEmpty()) {
                    globalProperty = propertyResolver.resolvePlaceholders(globalProperty);
                    globalFlag = Boolean.parseBoolean(globalProperty);
                }
                if (globalFlag) {
                    config.add(addon);
                    LOG.info("Marked addon {} as global", addon);
                } else {
                    LOG.info("Marked addon {} as not global", addon);
                }
            } else {
                LOG.debug("Ignoring addon {} as it does not have @Global annotation", beanName);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
