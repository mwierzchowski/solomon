package solomon.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import solomon.CommandDecorator;
import solomon.CommandResult;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class AfterDecorator implements CommandDecorator {
    private final @NonNull BiConsumer<Object, CommandResult> delegate;

    @Override
    public void after(Object command, CommandResult result) {
        delegate.accept(command, result);
    }
}
