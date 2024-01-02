package solomonx.api;

import java.util.function.BiConsumer;

public interface Runner<C, V> extends BiConsumer<C, OutputStore<V>> {
    default void safeAccept(C command, OutputStore<V> outputStore) {
        try {
            this.accept(command, outputStore);
        } catch (RuntimeException ex) {
            outputStore.setException(ex);
        }
    }
}
