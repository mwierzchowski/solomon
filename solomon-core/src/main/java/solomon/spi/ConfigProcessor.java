package solomon.spi;

import solomon.Config;

@FunctionalInterface
public interface ConfigProcessor {
    ConfigProcessor NO_OPS = (cmd, defaultCfg) -> defaultCfg;

    Config process(Object cmd, Config defaultCfg);
}
