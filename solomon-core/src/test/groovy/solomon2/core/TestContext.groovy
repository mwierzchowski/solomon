package solomon2.core

import groovy.transform.TupleConstructor
import solomon2.support.ContextAdapter

@TupleConstructor(includeFields = true)
 class TestContext extends ContextAdapter<Object> {
    def command
}
