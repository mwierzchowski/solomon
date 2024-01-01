package solomonx.api;

import lombok.NonNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface Bootstrapper {
    Caller<Runnable, Runnable> RUNNABLE_CALLER = (command, outputStore) -> {
        command.run();
        outputStore.setValue(command);
    };

    Caller<Supplier<?>, Object> SUPPLIER_CALLER = (command, outputStore) -> {
        var value = command.get();
        outputStore.setValue(value);
    };

    default <C extends Runnable> Configurer<C, C> runnable(Class<C> commandClass, Consumer<C>... initializers) {
        return this.bootstrap(commandClass, RUNNABLE_CALLER, initializers);
    }

    default  <C extends Supplier<V>, V> Configurer<C, V> supplier(Class<C> commandClass, Consumer<C>... initializers) {
        return this.bootstrap(commandClass, SUPPLIER_CALLER, initializers);
    }

    <C, V> Configurer<C, V> bootstrap(@NonNull Class<C> commandClass,Caller<? super C, ? super V> caller,
                                      Consumer<C>[] initializers);
}
