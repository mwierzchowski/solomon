package solomon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import solomon.addons.Addon;
import solomon.addons.Decorator;
import solomon.addons.Listener;

import static solomon.Utils.cast;

@Slf4j
@Getter
@AllArgsConstructor
public class Execution<C, V> extends Context<C> implements Flow<C, V> {
    private final C command;
    private final Handler<C, V> handler;
    private Config config;

    @Override
    public V execute() {
        LOG.debug("Execution started");
        Result<V> result = null;
        int decoratorCount = 0;
        boolean decoratorFailed = true;
        try {
            LOG.debug("Decorating before");
            for (int i = 0; this.config.contains(Decorator.class, i); i++, decoratorCount++) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.before(cast(this));
            }
            LOG.debug("Executed {} decorators", decoratorCount);
            decoratorFailed = false;
            LOG.debug("Running command");
            result = handler.apply(command);
        } catch (RuntimeException ex) {
            if (decoratorFailed) {
                decoratorCount += 1;
                LOG.debug("Exception in decorator on position {}: {}", decoratorCount, ex.getMessage());
            } else {
                LOG.debug("Exception in command: {}", ex.getMessage());
            }
            result = new Result<>(ex);
        } finally {
            assert result != null;
            LOG.debug("Decorating after");
            for (int i = decoratorCount - 1; this.config.contains(Decorator.class, i); i--) {
                Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
                decorator.safeAfter(cast(this), cast(result));
            }
        }
        int listenerCount = 0;
        if (result.isSuccess()) {
            LOG.debug("Sending success notification(s)");
            for (int i = 0; this.config.contains(Listener.class, i); i++, listenerCount++) {
                Listener<?, ?> listener = this.config.get(Listener.class, i);
                listener.safeOnSuccess(cast(this.command), cast(result.getValue()));
            }
        } else {
            LOG.debug("Sending failure notification(s)");
            for (int i = 0; this.config.contains(Listener.class, i); i++) {
                Listener<?, ?> listener = this.config.get(Listener.class, i);
                listener.safeOnFailure(cast(this.command), result.getException());
            }
            LOG.debug("Sent {} notifications", listenerCount);
        }
        LOG.debug("Execution finished");
        return result.getValueOrThrowException();
    }

    @Override
    public Context<C> getContext() {
        return this;
    }

    @Override
    public void updateConfig(Addon addon) {
        this.config = this.config.unlock();
        this.config.add(addon);
    }
}
