package solomon;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Result<V> {
    V getValue();
    RuntimeException getException();

    default boolean isSuccess() {
        return this.getException() == null;
    }

    default boolean isFailure() {
        return this.getException() != null;
    }

    default Result<V> or(V value) {
        if (this.isFailure()) {
            if (this instanceof MutableResult<V> mutableResult) {
                mutableResult.eraseFailure(value);
            } else {
                throw new UnsupportedOperationException("Result is immutable");
            }
        }
        return this;
    }

    default Result<V> orGet(Supplier<V> valueSupplier) {
        if (this.isFailure()) {
            if (this instanceof MutableResult<V> mutableResult) {
                mutableResult.eraseFailure(valueSupplier.get());
            } else {
                throw new UnsupportedOperationException("Result is immutable");
            }
        }
        return this;
    }

    default <X extends RuntimeException> Result<V> orThrow(Supplier<? extends X> exceptionSupplier) {
        if (this.isFailure()) {
            if (this instanceof MutableResult<V> mutableResult) {
                mutableResult.setException(exceptionSupplier.get());
            } else {
                throw new UnsupportedOperationException("Result is immutable");
            }
        }
        return this;
    }

    default <X extends RuntimeException> Result<V> orThrow(Function<RuntimeException, ? extends X> exceptionMapper) {
        if (this.isFailure()) {
            if (this instanceof MutableResult<V> mutableResult) {
                var currentException = this.getException();
                mutableResult.setException(exceptionMapper.apply(currentException));
            } else {
                throw new UnsupportedOperationException("Result is immutable");
            }
        }
        return this;
    }

    default V get() {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw this.getException();
        }
    }

    default <T> T get(Function<V, T> valueMapper) {
        return valueMapper.apply(this.get());
    }

    default Optional<V> getOptional() {
        return Optional.ofNullable(this.get());
    }

    default Stream<V> getStream() {
        return Stream.ofNullable(this.get());
    }

    default V orElse(V defaultValue) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            return defaultValue;
        }
    }

    default V orElseGet(Supplier<V> defaultValueSupplier) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            return defaultValueSupplier.get();
        }
    }

    default <X extends RuntimeException> V orElseThrow(Function<RuntimeException, X> exceptionMapper) throws X {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw exceptionMapper.apply(this.getException());
        }
    }

    default <X extends RuntimeException> V orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw exceptionSupplier.get();
        }
    }

    default void ifSuccess(Consumer<V> successConsumer) {
        if (this.isSuccess()) {
            successConsumer.accept(this.getValue());
        }
    }

    default void ifFailure(Consumer<RuntimeException> failureConsumer) {
        if (this.isFailure()) {
            failureConsumer.accept(this.getException());
        }
    }
}
