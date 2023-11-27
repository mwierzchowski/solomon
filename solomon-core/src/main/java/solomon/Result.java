package solomon;

import static solomon.Utils.cast;

public interface Result<V> {
    void setValue(V value);
    void setException(RuntimeException exception);
    V getValue();
    RuntimeException getException();

    default void setSuccess(V value) {
        this.setValue(value);
        this.setException(null);
    }

    default void setFailure(RuntimeException exception) {
        this.setException(exception);
    }

    default boolean isSuccess() {
        return this.getException() == null;
    }

    default boolean isFailure() {
        return !this.isSuccess();
    }

    default V getValueOrThrowException() {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw this.getException();
        }
    }

    default  <T> Result<T> asResult() {
        return cast(this);
    }
}
