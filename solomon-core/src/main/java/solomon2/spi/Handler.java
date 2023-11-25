package solomon2.spi;

import solomon2.core.Result;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Handler<C, V> extends Function<C, Result<V>> {
    Handler<? extends Runnable, ? extends Runnable> RUNNABLE = cmd -> {
        cmd.run();
        return new Result<>(cmd);
    };

    Handler<? extends Supplier<?>, ?> SUPPLIER = cmd -> {
        var value = cmd.get();
        return new Result<>(value);
    };
}
