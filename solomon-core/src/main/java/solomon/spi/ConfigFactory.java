package solomon.spi;

import solomon.Config;

@FunctionalInterface
public interface ConfigFactory {
    <C extends Config> C create(Class<C> clazz);
}
