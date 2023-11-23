package solomon2.core

import groovy.transform.TupleConstructor

@TupleConstructor(includeFields = true)
 class TestContext extends Context<Object> {
    def command
}
