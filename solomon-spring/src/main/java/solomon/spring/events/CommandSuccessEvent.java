package solomon.spring.events;

public record CommandSuccessEvent(Object command, Object value) implements CommandEvent {
    public <V> V valueAs(Class<V> valueClass) {
        return valueClass.cast(this.value);
    }
}
