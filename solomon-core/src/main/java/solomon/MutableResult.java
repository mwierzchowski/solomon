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

    static <T> MutableResult<T> asMutable(Result<T> result) {
        if (result instanceof MutableResult<T> mutableResult) {
            return mutableResult;
        } else {
            throw new UnsupportedOperationException("Result is immutable");
        }
    }

    static <T> MutableResult<T> asMutableResult(MutableResult<?> result) {
        return cast(result);
    }
}
