package solomon.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import solomon.CommandExecutor;
import solomon.Config;
import solomon.services.DefaultFactory;
import solomon.services.DefaultProcessor;
import solomon.services.Factory;
import solomon.services.Processor;

@Configuration
@ComponentScan
public class AutoConfiguration {
    @Bean("fallbackCommandFactory")
    public DefaultFactory defaultFactory() {
        return new DefaultFactory();
    }

    @Bean
    public DefaultProcessor defaultProcessor(Factory factory) {
        return new DefaultProcessor(factory);
    }

    @Bean
    public Config globalConfig() {
        return new Config();
    }

    @Lazy
    @Bean
    public CommandExecutor commandExecutor(Factory factory, Processor processor, Config globalConfig) {
        return CommandExecutor.builder()
                .withFactory(factory)
                .withProcessor(processor)
                .withGlobalConfig(globalConfig)
                .build();
    }
}
