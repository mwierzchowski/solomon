package solomon.addons;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import solomon.ExecutionContext;
import solomon.Result;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Decorators {
    public static <C, V> Decorator<C, V> before(@NonNull Consumer<ExecutionContext<? super C>> beforeHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void before(ExecutionContext<C> context) {
                beforeHandler.accept(context);
            }
        };
    }

    public static <C, V> Decorator<C, V> after(@NonNull BiConsumer<ExecutionContext<? super C>, Result<? super V>> afterHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void after(ExecutionContext<C> context, Result<V> result) {
                afterHandler.accept(context, result);
            }
        };
    }
}
