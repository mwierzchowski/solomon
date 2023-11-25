package solomon;

import lombok.NonNull;

public class Result<V> {
    private Object object;

    public Result(V value) {
        this.change(value);
    }

    public Result(RuntimeException exception) {
        this.change(exception);
    }

    public void change(V value) {
        this.object = value;
    }

    public void change(@NonNull RuntimeException exception) {
        this.object = exception;
    }

    public boolean changeIfSuccessful(RuntimeException ex) {
        if (this.isSuccess()) {
            this.change(ex);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public V getValue() {
        if (this.isFailure()) {
            throw new IllegalStateException("This result is a failure");
        }
        return (V) this.object;
    }

    public RuntimeException getException() {
        if (this.isSuccess()) {
            throw new IllegalStateException("This result is a success");
        }
        return (RuntimeException) this.object;
    }

    public V getValueOrThrowException() {
        if (this.isFailure()) {
            throw this.getException();
        }
        return this.getValue();
    }

    public boolean isFailure() {
        return this.object instanceof RuntimeException;
    }

    public boolean isSuccess() {
        return !this.isFailure();
    }
}
