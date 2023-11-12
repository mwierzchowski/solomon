package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import solomon.Decorator;
import solomon.Flow;

import java.util.List;

@Slf4j
public class RunnableFlow<C extends Runnable> extends Flow<RunnableFlow<C>, C, C> {
    public RunnableFlow(@NonNull C command, List<Decorator> globalDecorators) {
        super(command, globalDecorators);
    }

    @Override
    protected C internalExecute() {
        super.command.run();
        return super.command;
    }
}
