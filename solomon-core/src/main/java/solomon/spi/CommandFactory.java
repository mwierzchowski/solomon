package solomon.spi;

@FunctionalInterface
public interface CommandFactory {
    <C> C create(Class<C> clazz);
}
