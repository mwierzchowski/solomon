package solomon.tests;

import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class SupplierDecorator<C> implements Supplier<C> {
    private final Supplier<C> cmd;

    @Override
    public C get() {
        System.out.println("=== CLASS DECORATOR ===");
        var result = cmd.get();
        System.out.println("=======================");
        return result;
    }
}
