package solomonx.util;

import lombok.NonNull;
import solomonx.api.Context;
import solomonx.api.Decorator;
import solomonx.api.OutputStore;
import solomonx.support.DecoratorAdapter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Decorators {
    static <C, V> Decorator<C, V> before(@NonNull Consumer<Context<? super C>> beforeHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void before(Context<C> context) {
                beforeHandler.accept(context);
            }
        };
    }

    static <C, V> Decorator<C, V> after(@NonNull BiConsumer<Context<? super C>, OutputStore<? super V>> afterHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void after(Context<C> context, OutputStore<V> outputStore) {
                afterHandler.accept(context, outputStore);
            }
        };
    }
}
