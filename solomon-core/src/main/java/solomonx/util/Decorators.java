package solomonx.util;

import lombok.NonNull;
import solomonx.api.Context;
import solomonx.api.Decorator;
import solomonx.support.DecoratorAdapter;

import java.util.function.Consumer;

public interface Decorators {
    static <C, V> Decorator<C, V> before(@NonNull Consumer<Context<? super C, ? super V>> beforeHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void before(Context<C, V> context) {
                beforeHandler.accept(context);
            }
        };
    }

    static <C, V> Decorator<C, V> after(@NonNull Consumer<Context<? super C, ? super V>> afterHandler) {
        return new DecoratorAdapter<>() {
            @Override
            public void after(Context<C, V> context) {
                afterHandler.accept(context);
            }
        };
    }
}
