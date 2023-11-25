package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.CommandExecutor;
import solomon.spi.Factory;

@Configuration
public class SolomonAutoConfiguration {
    @Bean
    public Factory solomonFactory(ApplicationContext applicationContext) {
        return applicationContext::getBean;
    }

    @Bean
    public CommandExecutor solomonExecutor(Factory solomonFactory) {
        return CommandExecutor.builder().withFactory(solomonFactory).build();
    }
}
