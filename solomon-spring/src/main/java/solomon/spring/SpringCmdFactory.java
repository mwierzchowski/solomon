package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import solomon.CmdFactory;

@RequiredArgsConstructor
public class SpringCmdFactory extends CmdFactory {
    private final ApplicationContext context;

    @Override
    protected <C> C instantiateCommand(Class<C> clazz) {
        return context.getBean(clazz);
    }
}
