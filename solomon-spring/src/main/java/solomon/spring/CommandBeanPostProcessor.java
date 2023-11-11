package solomon.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import solomon.spring.annotation.Command;

public class CommandBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(Command.class)) {
            System.out.println("Processing bean: " + beanName);
        }
        return bean;
    }
}
