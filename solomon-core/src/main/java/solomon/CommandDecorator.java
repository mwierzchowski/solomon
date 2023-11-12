package solomon;

public interface CommandDecorator {
    default void before(Object command) {}
    default void after(Object command, Result result) {}
}
