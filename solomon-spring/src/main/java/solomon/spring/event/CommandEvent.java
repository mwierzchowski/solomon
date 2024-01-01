package solomon.spring.event;

public interface CommandEvent {
    Object command();

    default boolean isCommand(Class<?> commandClass) {
        return commandClass.isAssignableFrom(this.command().getClass());
    }

    default <C> C commandAs(Class<C> commandClass) {
        return commandClass.cast(this.command());
    }
}
