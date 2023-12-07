package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertyResolver;
import solomon.CommandExecutor;
import solomon.Config;
import solomon.services.DefaultFactory;
import solomon.services.DefaultProcessor;
import solomon.services.Factory;
import solomon.services.Processor;

@ComponentScan
@Configuration
public class AutoConfiguration {
    @Bean
    public GlobalAddonProcessor addonBeanProcessor(PropertyResolver propertyResolver, Config config) {
        return new GlobalAddonProcessor(propertyResolver, config);
    }

    @Bean
    public Factory commandFactory(ApplicationContext applicationContext) {
        var fallbackFactory = new DefaultFactory();
        return new SpringFactory(fallbackFactory, applicationContext);
    }

    @Bean
    public Processor commandProcessor(Factory commandFactory) {
        return new DefaultProcessor(commandFactory);
    }

    @Bean
    public Config commandConfig() {
        return new Config();
    }

    @Bean
    public CommandExecutor commandExecutor(Factory commandFactory, Processor commandProcessor, Config commandConfig) {
        return CommandExecutor.builder()
                .withFactory(commandFactory)
                .withProcessor(commandProcessor)
                .withConfig(commandConfig)
                .build();
    }
}
