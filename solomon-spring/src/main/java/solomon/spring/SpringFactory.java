package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import solomon.spi.Addon;
import solomon.spi.Factory;

@RequiredArgsConstructor
public class SpringFactory implements Factory {
    protected final Factory nonSpringFactory;
    protected final ApplicationContext springContext;

    @Override
    public <C> C getInstanceOf(Class<C> clazz) {
        C bean;
        try {
            bean = springContext.getBean(clazz);
        } catch (NoSuchBeanDefinitionException ex) {
            bean = nonSpringFactory.getInstanceOf(clazz);
        }
        return bean;
    }

    @Override
    public <A extends Addon> void register(A addon) {
        nonSpringFactory.register(addon);
    }
}
