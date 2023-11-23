package solomon2.spi;

import solomon2.core.ConfigSet;

public interface ConfigProcessor {
    ConfigSet process(Object command);
}
