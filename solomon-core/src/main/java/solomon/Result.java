package solomon;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Result<V> {
    private V value;
    private RuntimeException exception;
    private Long duration;
    private boolean overridden = false;

    public Result(V value, Long start) {
        this.value = value;
        this.exception = null;
        this.duration = System.currentTimeMillis() - start;
    }

    public Result(@NonNull RuntimeException exception, Long start) {
        this.value = null;
        this.exception = exception;
        if (start != null) {
            this.duration = System.currentTimeMillis() - start;
        }
    }

    public void overrideValue(V value) {
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
    public <T> T getValueOrThrowException() {
        if (this.isFailure()) {
            throw this.getException();
        }
        return (T) this.getValue();
    }

    public boolean isSuccess() {
        return this.exception == null;
    }

    public boolean isFailure() {
        return this.exception != null;
    }

    public boolean isDecorationFailure() {
        return duration == null;
    }
}
