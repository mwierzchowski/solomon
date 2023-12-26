package solomon;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.addons.Addon;
import solomon.addons.Decorator;
import solomon.addons.Observer;
import solomon.services.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNullElse;
import static solomon.ExecutionContext.asContext;
import static solomon.MutableResult.asResult;
import static solomon.Utils.cast;

@Data
@Slf4j
public class Execution<C, V> implements Flow<C, V>, ExecutionContext<C>, MutableResult<V> {
    @NonNull private final Factory factory;
    @NonNull private final C command;
    @NonNull private final Handler<C, V> handler;
    @NonNull private Config config;
    private Map<Object, Object> contextData;
    private V value;
    private Supplier<V> defaultValueSupplier;
    private RuntimeException exception;

    @Override
    public Result<V> execute() {
        LOG.debug("Execution started");
        int executedDecoratorsCount = 0;
        for (int i = 0; this.config.contains(Decorator.class, i); i++) {
            LOG.debug("Decorating before");
            Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
            decorator.safeBefore(asContext(this), asResult(this));
            executedDecoratorsCount += 1;
            if (this.isFailure()) {
                break;
            }
        }
        if (this.isSuccess()) {
            LOG.debug("Running command");
            this.handler.safeAccept(this.command, asResult(this));
        }
        for (int i = executedDecoratorsCount - 1; this.config.contains(Decorator.class, i); i--) {
            LOG.debug("Decorating after");
            Decorator<?, ?> decorator = this.config.get(Decorator.class, i);
            decorator.safeAfter(asContext(this), asResult(this));
        }
        for (int i = 0; this.config.contains(Observer.class, i); i++) {
            LOG.debug("Sending notification");
            Observer<?, ?> observer = this.config.get(Observer.class, i);
            observer.safeNotification(cast(this.command), asResult(this));
        }
        LOG.debug("Execution finished");
        return this;
    }

    @Override
    public Addon getAddon(Class<? extends Addon> addonClass) {
        return this.factory.getInstanceOf(addonClass);
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
