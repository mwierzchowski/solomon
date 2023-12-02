package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import solomon.addons.Addon;
import solomon.services.Factory;

@RequiredArgsConstructor
public class SpringFactory implements Factory {
    protected final Factory fallbackFactory;
    protected final ApplicationContext applicationContext;

    @Override
    public <C> C getInstanceOf(Class<C> clazz) {
        C bean;
        try {
            bean = applicationContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = fallbackFactory.getInstanceOf(clazz);
        }
        return bean;
    }

    @Override
    public <A extends Addon> void register(A addon) {
        fallbackFactory.register(addon);
    }
}
