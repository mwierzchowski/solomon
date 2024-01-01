package solomon.addons;

import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.function.BiConsumer;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Observers {
    public static <C, V> Observer<C, V> onSuccess(@NonNull BiConsumer<? super C, ? super V> successHandler) {
        return new ObserverAdapter<>() {
            @Override
            public void onSuccess(C command, V value) {
                successHandler.accept(command, value);
            }
        };
    }

    public static <C> Observer<C, Object> onFailure(@NonNull BiConsumer<C, RuntimeException> failureHandler) {
        return new ObserverAdapter<>() {
            @Override
            public void onFailure(C command, RuntimeException exception) {
                failureHandler.accept(command, exception);
            }
        };
    }
}
