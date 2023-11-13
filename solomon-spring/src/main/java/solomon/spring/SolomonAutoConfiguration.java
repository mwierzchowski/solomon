package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.Factory;

@Configuration
public class SolomonAutoConfiguration {
    @Bean
    public Factory commandExecutor(ApplicationContext applicationContext) {
        return new FactorySpring(applicationContext);
    }

    @Bean
    public CommandBeanPostProcessor commandBeanPostProcessor() {
        return new CommandBeanPostProcessor();
    }
}
