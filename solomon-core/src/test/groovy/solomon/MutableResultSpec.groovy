package solomon

import solomon.helpers.TestMutableResult
import spock.lang.Specification

class MutableResultSpec extends Specification {
    def result = new TestMutableResult()

    def "Stores command value"() {
        given:
        def value = 1
        when:
        result.setValue(value)
        then:
        result.get() == value
        result.isSuccess()
    }

    def "Stores command null value"() {
        when:
        result.setValue(null)
        then:
        result.get() == null
        result.isSuccess()
    }

    def "Stores command exception"() {
        given:
        def exception = new IllegalArgumentException()
        result.setException(exception)
        when:
        result.get()
        then:
        Exception ex = thrown()
        ex == exception
        result.isFailure()
    }

    def "Provides value for success"() {
        given:
        def value = new Object()
        result.setValue(value)
        expect:
        result.value() == value
    }

    def "Provides exception for failure"() {
        given:
        def exception = new RuntimeException()
        result.setException(exception)
        expect:
        result.exception() == exception
    }

    def "Erases exception"() {
        given:
        result.setException(new RuntimeException())
        when:
        result.eraseFailure()
        then:
        result.isSuccess()
    }
}