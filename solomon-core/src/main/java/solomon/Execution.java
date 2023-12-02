package solomon;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.addons.Decorator;
import solomon.addons.Listener;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNullElse;
import static solomon.Context.asContext;
import static solomon.Result.asResult;
import static solomon.Utils.cast;

@Data
@Slf4j
public class Execution<C, V> implements Flow<C, V>, Context<C>, Result<V> {
    @NonNull private final C command;
    @NonNull private final Handler<C, V> handler;
    @NonNull private Config config;
    private Map<Object, Object> contextData;
    private V value;
    private RuntimeException exception;

    @Override
    public V execute() {
        LOG.debug("Execution started");
        int decoratorCount = 0;
        boolean decoratorFailed = true;
        try {
            LOG.debug("Decorating before");
            for (int i = 0; this.config.contains(Decorator.class, i); i++, decoratorCount++) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.before(asContext(this));
            }
            LOG.debug("Executed {} decorators", decoratorCount);
            decoratorFailed = false;
            LOG.debug("Running command");
            this.handler.accept(this.command, asResult(this));
        } catch (RuntimeException ex) {
            if (decoratorFailed) {
                decoratorCount += 1;
                LOG.debug("Exception in decorator on position {}: {}", decoratorCount, ex.getMessage());
            } else {
                LOG.debug("Exception in command: {}", ex.getMessage());
            }
            this.exception = ex;
        } finally {
            LOG.debug("Decorating after");
            for (int i = decoratorCount - 1; this.config.contains(Decorator.class, i); i--) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.safeAfter(asContext(this), asResult(this));
            }
        }
        int listenerCount = 0;
        for (int i = 0; this.config.contains(Listener.class, i); i++, listenerCount++) {
            Listener<?, ?> listener = this.config.get(Listener.class, i);
            listener.send(cast(this.command), asResult(this));
        }
        LOG.debug("Sent {} notifications", listenerCount);
        LOG.debug("Execution finished");
        return this.getValueOrThrowException();
    }

    @Override
    public Config getConfig(boolean forUpdate) {
        if (forUpdate) {
            this.config = this.config.unlock();
        }
        return this.config;
    }

    @Override
    public Map<Object, Object> getContextData(boolean forUpdate) {
        if (this.contextData == null && forUpdate) {
            this.contextData = new HashMap<>();
        }
        return requireNonNullElse(this.contextData, emptyMap());
    }
}
