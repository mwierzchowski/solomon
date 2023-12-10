package solomon.spring.events;

public record CommandFailureEvent(Object command, RuntimeException exception) {
    public boolean isCommand(Class<?> commandClass) {
        return commandClass.isAssignableFrom(this.command.getClass());
    }

    public <C> C commandAs(Class<C> commandClass) {
        return commandClass.cast(this.command);
    }

    public <E extends RuntimeException> E exceptionAs(Class<E> exceptionClass) {
        return exceptionClass.cast(this.exception);
    }
}
