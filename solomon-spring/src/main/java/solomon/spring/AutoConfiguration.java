package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.CommandExecutor;
import solomon.spi.Factory;
import solomon.support.CacheableFactory;

@Configuration
public class AutoConfiguration {
    @Bean
    public Factory commandFactory(ApplicationContext context) {
        return new SpringFactory(CacheableFactory.getInstance(), context);
    }

    @Bean
    public CommandExecutor commandExecutor(Factory commandFactory) {
        return CommandExecutor.builder().withFactory(commandFactory).build();
    }
}
