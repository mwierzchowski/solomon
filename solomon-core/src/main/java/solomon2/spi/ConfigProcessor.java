package solomon2.spi;

import solomon2.core.configs.Config;

public interface ConfigProcessor {
    Config process(Object command, Config config);
}
