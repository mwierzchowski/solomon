package solomon2.support;

import solomon2.core.ConfigSet;
import solomon2.spi.ConfigProcessor;

public class NoOpsConfigProcessor implements ConfigProcessor {
    @Override
    public ConfigSet process(Object command) {
        return new ConfigSet(null);
    }
}
