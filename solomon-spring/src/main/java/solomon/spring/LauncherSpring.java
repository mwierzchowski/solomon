package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import solomon.Launcher;

@RequiredArgsConstructor
public class LauncherSpring extends Launcher {
    private final ApplicationContext context;

    @Override
    protected <C> C instantiateCommand(Class<C> clazz) {
        return context.getBean(clazz);
    }
}
