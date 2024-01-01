package solomonx.api;

@FunctionalInterface
public interface Caller<C, V> {
    default void callSafe(C command, OutputStore<V> outputStore) {
        try {
            this.call(command, outputStore);
        } catch (RuntimeException ex) {
            outputStore.setException(ex);
        }
    }

    void call(C command, OutputStore<V> outputStore);
}
