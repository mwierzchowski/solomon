package solomon;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static solomon.MutableResult.asMutable;

public interface Result<V> {
    V getValue();
    RuntimeException getException();

    default boolean isSuccess() {
        return this.getException() == null;
    }

    default boolean isFailure() {
        return this.getException() != null;
    }

    default Result<V> orDefault(V value) {
        if (this.isFailure()) {
            asMutable(this).eraseFailure(value);
        }
        return this;
    }

    default Result<V> orCompute(@NonNull Supplier<V> valueSupplier) {
        if (this.isFailure()) {
            var newValue = valueSupplier.get();
            asMutable(this).eraseFailure(newValue);
        }
        return this;
    }

    default <X extends RuntimeException> Result<V> orThrow(@NonNull Supplier<? extends X> exceptionSupplier) {
        if (this.isFailure()) {
            var newException = exceptionSupplier.get();
            asMutable(this).setException(newException);
        }
        return this;
    }

    default <X extends RuntimeException> Result<V> orThrow(@NonNull Function<RuntimeException, ? extends X> exceptionMapper) {
        if (this.isFailure()) {
            var currentException = this.getException();
            var newException = exceptionMapper.apply(currentException);
            asMutable(this).setException(newException);
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

    default V getOrDefault(V defaultValue) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            return defaultValue;
        }
    }

    default V getOrCompute(@NonNull Supplier<V> defaultValueSupplier) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            return defaultValueSupplier.get();
        }
    }

    default <X extends RuntimeException> V getOrThrow(@NonNull Function<RuntimeException, X> exceptionMapper) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw exceptionMapper.apply(this.getException());
        }
    }

    default <X extends RuntimeException> V getOrThrow(@NonNull Supplier<? extends X> exceptionSupplier) {
        if (this.isSuccess()) {
            return this.getValue();
        } else {
            throw exceptionSupplier.get();
        }
    }

    default <T> T map(@NonNull Function<V, T> valueMapper) {
        return valueMapper.apply(this.get());
    }

    default Optional<V> mapToOptional() {
        return Optional.ofNullable(this.get());
    }

    default Stream<V> mapToStream() {
        return Stream.ofNullable(this.get());
    }

    default void ifSuccess(@NonNull Consumer<V> successConsumer) {
        if (this.isSuccess()) {
            successConsumer.accept(this.getValue());
        }
    }

    default void ifFailure(@NonNull Consumer<RuntimeException> failureConsumer) {
        if (this.isFailure()) {
            failureConsumer.accept(this.getException());
        }
    }
}
