package solomon;

import static solomon.Utils.cast;

public interface Result<V> {
    Object getResultObject();
    void setResultObject(Object object);

    default void setValue(V value) {
        setResultObject(value);
    }

    default void setException(RuntimeException exception) {
        setResultObject(exception);
    }

    default V getValue() {
      return cast(getResultObject());
    }

    default RuntimeException getException() {
        return cast(getResultObject());
    }

    default boolean isFailure() {
        return getResultObject() instanceof RuntimeException;
    }

    default boolean isSuccess() {
        return !this.isFailure();
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
