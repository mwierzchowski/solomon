package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import solomon.CmdBuilder;

@RequiredArgsConstructor
public class SpringCmdBuilder extends CmdBuilder {
    private final ApplicationContext context;

//    @Override
//    protected <C> C instantiateCommand(Class<C> clazz) {
//        return context.getBean(clazz);
//    }
}
