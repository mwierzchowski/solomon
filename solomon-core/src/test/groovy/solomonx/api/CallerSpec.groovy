package solomonx.api

import spock.lang.Specification

class CallerSpec extends Specification {
    def caller = Spy(Caller)
    def command = new Object()
    def outputStore = Mock(OutputStore)

    def "Safe call catches RuntimeException"() {
        given:
        def exception = new RuntimeException("test")
        caller.call(command, outputStore) >> {
            throw exception
        }
        when:
        caller.callSafe(command, outputStore)
        then:
        noExceptionThrown()
        1 * outputStore.setException(exception)
    }

}