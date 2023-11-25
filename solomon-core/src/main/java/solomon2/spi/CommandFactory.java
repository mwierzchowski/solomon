package solomon2.spi;

public interface CommandFactory {
    <C> C instantiate(Class<C> clazz);
}
