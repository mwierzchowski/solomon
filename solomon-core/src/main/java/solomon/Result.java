package solomon;

import lombok.Getter;
import lombok.NonNull;

public class Result {
    private Object value;
    private RuntimeException exception;
    @Getter private boolean overridden;
    @Getter private final long duration;

    public Result(Object value, long start) {
        this.value = value;
        this.exception = null;
        this.duration = System.currentTimeMillis() - start;
        this.overridden = false;
    }

    public Result(@NonNull RuntimeException exception, long start) {
        this.value = null;
        this.exception = exception;
        this.duration = System.currentTimeMillis() - start;
        this.overridden = false;
    }

    public void overrideValue(Object value) {
        this.value = value;
        this.exception = null;
        this.overridden = true;
    }

    public void overrideException(@NonNull RuntimeException exception) {
        this.exception = exception;
        this.value = null;
        this.overridden = true;
    }

    @SuppressWarnings("unchecked")
    public <V> V getValue() {
        return (V) this.value;
    }

    @SuppressWarnings("unchecked")
    public <E extends RuntimeException> E getException() {
        return (E) this.exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isFailure() {
        return exception != null;
    }
}
