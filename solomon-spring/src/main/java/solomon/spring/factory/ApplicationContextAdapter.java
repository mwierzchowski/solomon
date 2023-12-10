package solomon.spring.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import solomon.addons.Addon;
import solomon.services.Factory;

@Primary
@Service
@RequiredArgsConstructor
public class ApplicationContextAdapter implements Factory {
    protected final ApplicationContext applicationContext;
    protected final Factory fallbackCommandFactory;

    @Override
    public <C> C getInstanceOf(Class<C> clazz) {
        C bean;
        try {
            bean = applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = fallbackCommandFactory.getInstanceOf(clazz);
        }
        return bean;
    }

    @Override
    public <A extends Addon> void cache(A addon) {
        fallbackCommandFactory.cache(addon);
    }
}
