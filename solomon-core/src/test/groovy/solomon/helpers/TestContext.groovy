package solomon.helpers

import groovy.transform.TupleConstructor
import solomon.Context

@TupleConstructor(includeFields = true)
 class TestContext implements Context<Object> {
    Object command
    Map<Object, Object> contextData;
}
