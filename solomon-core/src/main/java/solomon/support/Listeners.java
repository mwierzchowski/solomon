package solomon.support;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import solomon.spi.Listener;

import java.util.function.BiConsumer;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Listeners {
    public static <C, V> Listener<C, V> onSuccess(@NonNull BiConsumer<? super C, ? super V> successHandler) {
        return new ListenerAdapter<>() {
            @Override
            public void onSuccess(C command, V value) {
                successHandler.accept(command, value);
            }
        };
    }

    public static <C> Listener<C, Object> onFailure(@NonNull BiConsumer<C, RuntimeException> failureHandler) {
        return new ListenerAdapter<>() {
            @Override
            public void onFailure(C command, RuntimeException exception) {
                failureHandler.accept(command, exception);
            }
        };
    }
}
