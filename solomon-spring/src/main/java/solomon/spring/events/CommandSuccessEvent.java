package solomon.spring.events;

public record CommandSuccessEvent(Object command, Object value) {
    public boolean isCommand(Class<?> commandClass) {
        return commandClass.isAssignableFrom(this.command.getClass());
    }

    public <C> C commandAs(Class<C> commandClass) {
        return commandClass.cast(this.command);
    }

    public <V> V valueAs(Class<V> valueClass) {
        return valueClass.cast(this.value);
    }
}
