package solomon.spring.annotation;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.PropertyResolver;
import org.springframework.stereotype.Service;
import solomon.Config;
import solomon.addons.Addon;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class GlobalBeanProcessor implements BeanPostProcessor {
    private final PropertyResolver propertyResolver;
    private final Config config;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
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
