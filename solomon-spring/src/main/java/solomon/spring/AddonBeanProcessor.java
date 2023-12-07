package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.PropertyResolver;
import solomon.Config;
//import solomon.spring.annotation.Addon;

//@Slf4j
@RequiredArgsConstructor
public class AddonBeanProcessor implements BeanPostProcessor {
    private final PropertyResolver propertyResolver;
    private final Config config;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (bean instanceof solomon.addons.Addon addon) {
//            var annotation = getClass().getAnnotation(Addon.class);
//            if (annotation != null) {
//                var useGlobally = annotation.useGlobally();
//                var useGloballyString = annotation.useGloballyString();
//                if (useGloballyString != null) {
//                    useGloballyString = propertyResolver.resolvePlaceholders(useGloballyString);
//                    useGlobally = Boolean.parseBoolean(useGloballyString);
//                }
//                if (useGlobally) {
//                    config.add(addon);
//                }
//            }
//        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
