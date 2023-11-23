package solomon2.spi;

public interface CmdFactory {
    <C> C instantiate(Class<C> clazz);
}
