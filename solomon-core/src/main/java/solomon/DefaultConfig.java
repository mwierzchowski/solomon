package solomon;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DefaultConfig implements Config {
    private List<Decorator<?, ?>> decorators = new ArrayList<>();

    public DefaultConfig addDecorator(Decorator<?, ?> decorator) {
        decorators.add(decorator);
        return this;
    }

    @Override
    public <F, C, V> void apply(Flow<F, C, V> flow) {
        decorators.forEach(flow::decorate);
        LOG.debug("Applied configuration");
    }
}
