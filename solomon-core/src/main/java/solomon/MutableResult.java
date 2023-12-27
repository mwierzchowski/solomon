package solomon;

import static solomon.Utils.cast;

public interface MutableResult<V> extends Result<V> {
    void setValue(V value);
    void setException(RuntimeException exception);


    default void eraseFailure() {
        this.setException(null);
    }

    default void eraseFailure(V value) {
        this.setValue(value);
        this.eraseFailure();
    }

    static <T> MutableResult<T> asResult(MutableResult<?> result) {
        return cast(result);
    }
}
