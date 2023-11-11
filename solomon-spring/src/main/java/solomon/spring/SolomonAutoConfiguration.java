package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.CommandExecutor;

@Configuration
public class SolomonAutoConfiguration {
    @Bean
    public CommandExecutor commandExecutor(ApplicationContext applicationContext) {
        return new CommandExecutorSpring(applicationContext);
    }

    @Bean
    public CommandBeanPostProcessor commandBeanPostProcessor() {
        return new CommandBeanPostProcessor();
    }
}
