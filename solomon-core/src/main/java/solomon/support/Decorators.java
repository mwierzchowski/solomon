package solomon.support;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import solomon.spi.Context;
import solomon.Result;
import solomon.spi.Decorator;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Decorators {
    public static <C, V> Decorator<C, V> before(@NonNull Consumer<Context<? super C>> beforeHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void before(Context<C> context) {
                beforeHandler.accept(context);
            }
        };
    }

    public static <C, V> Decorator<C, V> after(@NonNull BiConsumer<Context<? super C>, Result<? super V>> afterHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void after(Context<C> context, Result<V> result) {
                afterHandler.accept(context, result);
            }
        };
    }
}
