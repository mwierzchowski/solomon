package solomon2.spi;

import solomon2.ExecutionResult;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Handler<C, V> extends Function<C, ExecutionResult<V>> {
    Handler<? extends Runnable, ? extends Runnable> RUNNABLE = cmd -> {
        cmd.run();
        return new ExecutionResult<>(cmd);
    };

    Handler<? extends Supplier<?>, ?> SUPPLIER = cmd -> {
        var value = cmd.get();
        return new ExecutionResult<>(value);
    };
}
