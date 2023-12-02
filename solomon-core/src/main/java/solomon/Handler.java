package solomon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface Handler<C, V> extends BiConsumer<C, Result<V>> {
    Logger LOG = LoggerFactory.getLogger(Handler.class);

    default void safeAccept(C command, Result<V> result) {
        try {
            this.accept(command, result);
        } catch (RuntimeException ex) {
            LOG.debug("Exception in command: {}", ex.getMessage());
            result.setException(ex);
        }
    }

    Handler<? extends Runnable, ? extends Runnable> RUNNABLE = (cmd, result) -> {
        cmd.run();
        result.setValue(cmd);
    };

    Handler<? extends Supplier<?>, ?> SUPPLIER = (cmd, result) -> result.setValue(cmd.get());
}
