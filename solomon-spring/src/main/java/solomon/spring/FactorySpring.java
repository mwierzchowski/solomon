package solomon.spring;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import solomon.Factory;

@RequiredArgsConstructor
public class FactorySpring extends Factory {
    private final ApplicationContext context;

    @Override
    protected <C> C instantiateCommand(Class<C> clazz) {
        return context.getBean(clazz);
    }
}
