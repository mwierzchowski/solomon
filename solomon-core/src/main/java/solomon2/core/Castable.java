package solomon2.core;

public interface Castable {
    @SuppressWarnings("unchecked")
    default <T> T cast() {
        return (T) this;
    }
}
