package solomon;

public interface Decorator<C, V> {
    default void before(C command) {}
    default void after(C command, Result<V> result) {}
}
