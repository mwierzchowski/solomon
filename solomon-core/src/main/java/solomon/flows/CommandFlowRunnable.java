package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.CommandDecorator;
import solomon.CommandFlow;

import java.util.List;

@Slf4j
public class CommandFlowRunnable<C extends Runnable> extends CommandFlow<CommandFlowRunnable<C>, C, C> {
    public CommandFlowRunnable(@NonNull C command, List<CommandDecorator> globalDecorators) {
        super(command, globalDecorators);
    }

    @Override
    protected C internalExecute() {
        super.command.run();
        return super.command;
    }
}
