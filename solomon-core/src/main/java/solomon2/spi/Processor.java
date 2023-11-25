package solomon2.spi;

import solomon2.configs.Config;

@FunctionalInterface
public interface Processor {
    Config process(Object command, Config config);
}
