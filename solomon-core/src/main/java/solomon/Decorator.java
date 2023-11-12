package solomon;

public interface Decorator<C> {
    default void before(C command) {}
    default void after(C command, Result result) {}
}
