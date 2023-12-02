package solomon

import solomon.helpers.TestResult
import spock.lang.Specification

class ResultSpec extends Specification {
    def result = new TestResult()

    def "Stores command value"() {
        given:
        def value = 1
        when:
        result.setValue(value)
        then:
        result.getValueOrThrowException() == value
        result.isSuccess()
    }

    def "Stores command null value"() {
        when:
        result.setValue(null)
        then:
        result.getValueOrThrowException() == null
        result.isSuccess()
    }

    def "Stores command exception"() {
        given:
        def exception = new IllegalArgumentException()
        result.setException(exception)
        when:
        result.getValueOrThrowException()
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
        result.getValue() == value
    }

    def "Provides exception for failure"() {
        given:
        def exception = new RuntimeException()
        result.setException(exception)
        expect:
        result.getException() == exception
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