package solomon.spring.event;

public record CommandSuccessEvent(Object command, Object value) implements CommandEvent {
    public <V> V valueAs(Class<V> valueClass) {
        return valueClass.cast(this.value);
    }
}
