package solomonx;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.Config;
import solomon.services.Factory;
import solomon.services.Processor;
import solomonx.api.*;
import solomonx.spi.AbstractBootstrap;

import java.util.function.Consumer;

@Slf4j
@Builder
public class Bootstrap extends AbstractBootstrap {
    private final Factory factory;
    private final Processor processor;
    private final Config globalConfig;

    @Override
    protected <C, V> Flow<C, V> createFlow(@NonNull Class<C> commandClass, @NonNull Runner<C, V> caller, Consumer<C>[] initializers) {
        LOG.debug("Building command: {}", commandClass.getSimpleName());
        var command = this.factory.getInstanceOf(commandClass);
        var execution = new Execution<>(command, caller);
        for (int i = 0; i < initializers.length; i++) {
            execution.setup(initializers[i]);
        }
        return execution;
    }

    @Data
    private class Execution<C, V> implements Flow<C, V>, OutputStore<V>, Context<C> {
        private final C command;
        private final Runner<C, V> caller;
        private V value;
        private RuntimeException exception;

        @Override
        public Output<V> execute() {
            LOG.debug("Execution started");
            // TODO Decorators before
            if (this.isSuccess()) {
                LOG.debug("Running command");
                this.caller.safeAccept(this.command, this);
            }
            // TODO Decorators after
            // TODO Observers
            LOG.debug("Execution finished");
            return this;
        }
    }
}
