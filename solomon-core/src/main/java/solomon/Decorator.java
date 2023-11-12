package solomon;

public interface Decorator {
    default void before(Object command) {}
    default void after(Object command, Result result) {}
}
