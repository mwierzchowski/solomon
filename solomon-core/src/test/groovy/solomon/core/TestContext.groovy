package solomon.core

import groovy.transform.TupleConstructor
import solomon.support.ContextAdapter

@TupleConstructor(includeFields = true)
 class TestContext extends ContextAdapter<Object> {
    def command
}
