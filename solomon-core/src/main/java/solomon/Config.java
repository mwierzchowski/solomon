package solomon;

import java.util.function.Consumer;

@FunctionalInterface
public interface Config {
    <F, C, V> void apply(Flow<F, C, V> flow);
}
