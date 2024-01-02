package solomonx.spi;

import lombok.NonNull;
import solomonx.api.Flow;
import solomonx.api.OutputStore;
import solomonx.util.Cast;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractBootstrap {
    protected interface Runner<C, V> extends BiConsumer<C, OutputStore<V>> {
        default void safeAccept(C command, OutputStore<V> outputStore) {
            try {
                this.accept(command, outputStore);
            } catch (RuntimeException ex) {
                outputStore.setException(ex);
            }
        }
    }

    private static final Runner<? extends Runnable, ? extends Runnable> RUNNABLE_RUNNER = (command, outputStore) -> {
        command.run();
        outputStore.setValue(command);
    };

    private static final Runner<Supplier<?>, Object> SUPPLIER_RUNNER = (command, outputStore) -> {
        var value = command.get();
        outputStore.setValue(value);
    };

    public <C extends Runnable> Flow<C, C> runnable(Class<C> commandClass, Consumer<C>... initializers) {
        return createFlow(commandClass, Cast.unchecked(RUNNABLE_RUNNER), initializers);
    }

    public  <C extends Supplier<V>, V> Flow<C, V> supplier(Class<C> commandClass, Consumer<C>... initializers) {
        return createFlow(commandClass, Cast.unchecked(SUPPLIER_RUNNER), initializers);
    }

    protected abstract <C, V> Flow<C, V> createFlow(@NonNull Class<C> commandClass, @NonNull Runner<C, V> caller,
                                                   Consumer<C>[] initializers);
}
