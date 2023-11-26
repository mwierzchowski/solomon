package solomon.services;

import solomon.Config;

@FunctionalInterface
public interface Processor {
    Config process(Object command, Config globalConfig);
}
