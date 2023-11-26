package solomon.spi;

public interface Factory {
    <C> C getInstanceOf(Class<C> clazz);
    <A extends Addon> void register(A addon);
}
