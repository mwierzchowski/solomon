package solomon.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import solomon.Decorator;
import solomon.Result;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class AfterDecorator implements Decorator {
    private final @NonNull BiConsumer<Object, Result> delegate;

    @Override
    public void after(Object command, Result result) {
        delegate.accept(command, result);
    }
}
