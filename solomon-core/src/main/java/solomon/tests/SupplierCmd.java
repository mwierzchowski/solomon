package solomon.tests;

import java.util.function.Supplier;

public class SupplierCmd implements Supplier<Integer> {
    public Integer x;

    @Override
    public Integer get() {
        return x * 100;
    }
}
