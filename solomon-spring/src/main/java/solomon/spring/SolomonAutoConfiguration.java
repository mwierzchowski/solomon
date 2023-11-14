package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.CmdBuilder;

@Configuration
public class SolomonAutoConfiguration {
    @Bean
    public CmdBuilder commandExecutor(ApplicationContext applicationContext) {
        return new SpringCmdBuilder(applicationContext);
    }

    @Bean
    public CommandBeanPostProcessor commandBeanPostProcessor() {
        return new CommandBeanPostProcessor();
    }
}
