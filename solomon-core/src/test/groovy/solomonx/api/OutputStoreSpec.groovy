package solomonx.api

import spock.lang.Specification

// TODO remove ??
class OutputStoreSpec extends Specification {
    def "Erase failure"() {
        given:
        def value = new Object()
        def outputStore = new TestOutputStore().tap {
            exception = new RuntimeException()
        }
        when:
        outputStore.eraseFailure(value)
        then:
        outputStore.value == value
        outputStore.exception == null
    }

    static class TestOutputStore implements OutputStore<Object> {
        Object value
        RuntimeException exception
    }
}