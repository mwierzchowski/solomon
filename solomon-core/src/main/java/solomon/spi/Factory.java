package solomon.spi;

@FunctionalInterface
public interface Factory {
    <C> C instantiate(Class<C> clazz);
}
