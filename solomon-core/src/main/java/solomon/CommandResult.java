package solomon;

import lombok.Getter;
import lombok.NonNull;

public class CommandResult {
    private Object value;
    private Exception exception;
    @Getter private boolean overridden;
    @Getter private final long duration;

    public CommandResult(@NonNull Object value, long start, long stop) {
        this.value = value;
        this.exception = null;
        this.duration = stop - start;
        this.overridden = false;
    }

    public CommandResult(@NonNull Exception exception, long start, long stop) {
        this.value = null;
        this.exception = exception;
        this.duration = stop - start;
        this.overridden = false;
    }

    public void overrideValue(@NonNull Object value) {
        this.value = value;
        this.exception = null;
        this.overridden = true;
    }

    public void overrideException(@NonNull Exception exception) {
        this.exception = exception;
        this.value = null;
        this.overridden = true;
    }

    @SuppressWarnings("unchecked")
    public <V> V getValue() {
        return (V) this.value;
    }

    @SuppressWarnings("unchecked")
    public <E extends Exception> E getException() {
        return (E) this.exception;
    }

    public boolean isSuccess() {
        return exception == null;
    }

    public boolean isFailure() {
        return exception != null;
    }
}
