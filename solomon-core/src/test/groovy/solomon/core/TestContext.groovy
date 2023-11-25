package solomon.core

import groovy.transform.TupleConstructor
import solomon.Context

@TupleConstructor(includeFields = true)
 class TestContext extends Context<Object> {
    def command
}
