package solomon

import solomon.helpers.TestExecutionContext
import spock.lang.Specification

class ExecutionContextSpec extends Specification {
    def context = new TestExecutionContext()

    def "Lazy initializes storage"() {
        expect:
        context.contextData == null
    }

    def "Storage is empty map when not initialized"() {
        when:
        def data = context.getContextData(false)
        then:
        data != null
        data == Collections.emptyMap()
    }

    def "Instantiates storage for update"() {
        when:
        context.getContextData(true)
        then:
        context.contextData != null
    }

    def "Does not instantiate storage for read"() {
        when:
        context.getContextData(false)
        then:
        context.contextData == null
    }
}