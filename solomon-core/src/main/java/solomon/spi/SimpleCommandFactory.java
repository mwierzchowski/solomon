package solomon.spi;

import solomon.utils.Instances;

public class SimpleCommandFactory implements CommandFactory {
    @Override
    public <C> C create(Class<C> clazz) {
        return Instances.instantiate(clazz);
    }
}
