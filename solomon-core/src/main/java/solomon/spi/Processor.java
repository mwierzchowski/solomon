package solomon.spi;

import solomon.configs.Config;

@FunctionalInterface
public interface Processor {
    Config process(Object command, Config config);
}
