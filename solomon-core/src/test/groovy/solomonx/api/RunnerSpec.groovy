package solomonx.api

import solomonx.spi.Runner
import spock.lang.Specification

// TODO remove ?? replace ??
class RunnerSpec extends Specification {
    def caller = Spy(Runner)
    def command = new Object()
    def outputStore = Mock(OutputStore)

    def "Safe call catches RuntimeException"() {
        given:
        def exception = new RuntimeException("test")
        caller.call(command, outputStore) >> {
            throw exception
        }
        when:
        caller.safeAccept(command, outputStore)
        then:
        noExceptionThrown()
        1 * outputStore.setException(exception)
    }

}