package solomonx.api;

public interface OutputStore<V> extends Output<V> {
    default void eraseFailure(V value) {
        this.setValue(value);
        this.setException(null);
    }

    void setValue(V value);
    void setException(RuntimeException exception);
}
