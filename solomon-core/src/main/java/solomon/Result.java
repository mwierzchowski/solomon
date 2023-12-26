package solomon;

import lombok.Data;

import java.util.Optional;
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

    default V get() {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw this.getException();
        }
    }

    default <T> T map(Function<V, T> valueMapper) {
        return valueMapper.apply(this.get());
    }

    default Optional<V> optional() {
        return Optional.ofNullable(this.get());
    }

    default Stream<V> stream() {
        return Stream.ofNullable(this.get());
    }

    default Result<V> or(V value) {
        if (this.isSuccess()) {
            return this;
        } else {
            return new ResultRecord<>(value, null);
        }
    }

    default Result<V> or(Supplier<V> valueSupplier) {
        if (this.isSuccess()) {
            return this;
        } else {
            return new ResultRecord<>(valueSupplier.get(), null);
        }
    }

    default <X extends RuntimeException> Result<V> orThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.isSuccess()) {
            return this;
        } else {
            return new ResultRecord<>(null, exceptionSupplier.get());
        }
    }

    default <X extends RuntimeException> Result<V> orThrow(Function<RuntimeException, ? extends X> exceptionMapper) throws X {
        if (this.isSuccess()) {
            return this;
        } else {
            return new ResultRecord<>(null, exceptionMapper.apply(this.getException()));
        }
    }

    default V orElse(V defaultValue) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            return defaultValue;
        }
    }

    default V orElse(Supplier<V> defaultValueSupplier) {
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

    @Data
    class ResultRecord<V> implements Result<V> {
        final V value;
        final RuntimeException exception;
    }
}
