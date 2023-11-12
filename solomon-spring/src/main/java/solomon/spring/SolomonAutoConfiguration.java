package solomon.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import solomon.Launcher;

@Configuration
public class SolomonAutoConfiguration {
    @Bean
    public Launcher commandExecutor(ApplicationContext applicationContext) {
        return new LauncherSpring(applicationContext);
    }

    @Bean
    public CommandBeanPostProcessor commandBeanPostProcessor() {
        return new CommandBeanPostProcessor();
    }
}
