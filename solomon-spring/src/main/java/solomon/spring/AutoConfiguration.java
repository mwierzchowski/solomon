package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.CommandExecutor;
import solomon.services.DefaultFactory;
import solomon.services.DefaultProcessor;
import solomon.services.Factory;
import solomon.services.Processor;

@Configuration
public class AutoConfiguration {
    @Bean
    public Factory commandFactory(ApplicationContext context) {
        var fallbackFactory = new DefaultFactory();
        return new SpringFactory(fallbackFactory, context);
    }

    @Bean
    public Processor commandProcessor(Factory commandFactory) {
        return new DefaultProcessor(commandFactory);
    }

    @Bean
    public CommandExecutor commandExecutor(Factory commandFactory, Processor commandProcessor) {
        return CommandExecutor.builder()
                .withFactory(commandFactory)
                .withProcessor(commandProcessor)
                .build();
    }
}
