package solomon.spring.adapter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import solomon.addons.CountingObserver;
import solomon.spring.annotation.Addon;
import solomon.spring.annotation.Global;

@Addon
@ConditionalOnProperty(name = "solomon.counting.enabled", matchIfMissing = true)
@Global(onProperty = "solomon.counting.global")
public class CountingObserverAdapter extends CountingObserver {
}
