package solomon;

import static solomon.Utils.cast;

public interface MutableResult<V> extends Result<V> {
    void setValue(V value);
    void setException(RuntimeException exception);


    default void eraseFailure() {
        this.setException(null);
    }

    static <T> MutableResult<T> asResult(MutableResult<?> result) {
        return cast(result);
    }
}
