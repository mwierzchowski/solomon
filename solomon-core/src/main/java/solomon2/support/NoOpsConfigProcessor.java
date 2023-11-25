package solomon2.support;

import solomon2.core.configs.Config;
import solomon2.spi.ConfigProcessor;

public class NoOpsConfigProcessor implements ConfigProcessor {
    @Override
    public Config process(Object command, Config config) {
        return config;
    }
}
