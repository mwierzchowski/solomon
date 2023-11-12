package solomon.decorators;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import solomon.Decorator;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class BeforeDecorator implements Decorator {
    private final @NonNull Consumer<Object> delegate;

    @Override
    public void before(Object command) {
        delegate.accept(command);
    }
}
