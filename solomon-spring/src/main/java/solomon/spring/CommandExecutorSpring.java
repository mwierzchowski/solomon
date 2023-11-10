package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import solomon.CommandExecutor;

@RequiredArgsConstructor
public class CommandExecutorSpring extends CommandExecutor {
    private final ApplicationContext context;

    @Override
    protected <C> C instantiateCommand(Class<C> clazz) {
        return context.getBean(clazz);
    }
}
