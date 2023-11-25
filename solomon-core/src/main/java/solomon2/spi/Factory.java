package solomon2.spi;

@FunctionalInterface
public interface Factory {
    <C> C instantiate(Class<C> clazz);
}
