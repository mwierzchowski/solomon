package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.CmdFactory;

@Configuration
public class SolomonAutoConfiguration {
    @Bean
    public CmdFactory commandExecutor(ApplicationContext applicationContext) {
        return new SpringCmdFactory(applicationContext);
    }

    @Bean
    public CommandBeanPostProcessor commandBeanPostProcessor() {
        return new CommandBeanPostProcessor();
    }
}
