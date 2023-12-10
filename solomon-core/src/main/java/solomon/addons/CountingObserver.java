package solomon.addons;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import static solomon.Utils.shortNameFor;

@Slf4j
@Getter
public class CountingObserver implements Observer<Object, Object> {
    protected final Counter globalCounter = new Counter();
    protected final ConcurrentMap<Class<?>, Counter> commandCounter = new ConcurrentHashMap<>();

    @Override
    public void onSuccess(Object command, Object value) {
        this.globalCounter.successes.incrementAndGet();
        var successes = this.counterFor(command).successes.incrementAndGet();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Command {} finished with success {} times", shortNameFor(command), successes);
        }
    }

    @Override
    public void onFailure(Object command, RuntimeException exception) {
        this.globalCounter.failures.incrementAndGet();
        var failures = this.counterFor(command).failures.incrementAndGet();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Command {} finished with failure {} times", shortNameFor(command), failures);
        }
    }

    protected Counter counterFor(Object command) {
        return this.commandCounter.computeIfAbsent(command.getClass(), aClass -> new Counter());
    }

    @Data
    public static class Counter {
        AtomicInteger successes = new AtomicInteger(0);
        AtomicInteger failures = new AtomicInteger(0);

        public int countAll() {
            return successes.get() + failures.get();
        }
    }
}
