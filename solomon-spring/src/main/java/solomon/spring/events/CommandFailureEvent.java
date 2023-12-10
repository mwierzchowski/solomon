package solomon.spring.events;

public record CommandFailureEvent(Object command, RuntimeException exception) implements CommandEvent {
    public <E extends RuntimeException> E exceptionAs(Class<E> exceptionClass) {
        return exceptionClass.cast(this.exception);
    }
}
