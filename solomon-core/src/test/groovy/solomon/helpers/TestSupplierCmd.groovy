package solomon.helpers

import groovy.transform.TupleConstructor

import java.util.function.Supplier

@TupleConstructor(includeFields = true)
class TestSupplierCmd implements Supplier<Integer> {
    def x = 0

    @Override
    Integer get() {
        return x
    }
}
