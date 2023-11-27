package solomon;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface Handler<C, V> extends BiConsumer<C, Result<V>> {
    Handler<? extends Runnable, ? extends Runnable> RUNNABLE = (cmd, result) -> {
        cmd.run();
        result.setValue(cmd);
    };

    Handler<? extends Supplier<?>, ?> SUPPLIER = (cmd, result) -> {
        result.setValue(cmd.get());
    };
}
