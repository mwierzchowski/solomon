package solomon;

import lombok.Setter;
import solomon.flows.RunnableFlow;
import solomon.flows.SupplierFlow;
import solomon.spi.*;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static solomon.utils.Instances.instantiate;

@Setter
public class CmdBuilder {
    private final CommandFactory cmdFactory;
    private final ConfigFactory cfgFactory;
    private final ConfigProcessor cfgProcessor;
    private final DefaultConfig defaultConfig;

    public CmdBuilder() {
        this.cmdFactory = new SimpleCommandFactory();
        this.cfgFactory = new SimpleConfigFactory();
        this.cfgProcessor = ConfigProcessor.NO_OPS;
        this.defaultConfig = this.cfgFactory.create(DefaultConfig.class);
    }

    public DefaultConfig defaultConfig() {
        return this.defaultConfig;
    }

    public <C extends Runnable> RunnableFlow<C> createRunnable(Class<C> clazz) {
        C cmd = this.cmdFactory.create(clazz);
        Config cfg = this.cfgProcessor.process(cmd, this.defaultConfig);
        RunnableFlow<C> flow = new RunnableFlow<>(cmd, null); // TODO remove?
        cfg.apply(flow);
        return flow;
    }

    public <C extends Runnable> RunnableFlow<C> createRunnable(Class<C> clazz, Consumer<C> initializer) {
        return createRunnable(clazz).setup(initializer);
    }

    public <C extends Supplier<O>, O> SupplierFlow<C, O> createSupplier(Class<C> clazz) {
        C command = instantiate(clazz);
        return new SupplierFlow<>(command, null);
    }

    public <C extends Supplier<O>, O> SupplierFlow<C, O> createSupplier(Class<C> clazz, Consumer<C> initializer) {
        return createSupplier(clazz).setup(initializer);
    }
}