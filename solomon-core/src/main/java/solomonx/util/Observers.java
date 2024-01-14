package solomonx.util;

import lombok.NonNull;
import solomonx.api.Observer;
import solomonx.support.ObserverAdapter;

import java.util.function.BiConsumer;

public interface Observers {
    static <C, V> Observer<C, V> onSuccess(@NonNull BiConsumer<C, V> successHandler) {
        return new ObserverAdapter<>() {
            @Override
            public void onSuccess(C command, V value) {
                successHandler.accept(command, value);
            }
        };
    }

    static <C> Observer<C, Object> onFailure(@NonNull BiConsumer<C, RuntimeException> failureHandler) {
        return new ObserverAdapter<>() {
            @Override
            public void onFailure(C command, RuntimeException exception) {
                failureHandler.accept(command, exception);
            }
        };
    }
}
