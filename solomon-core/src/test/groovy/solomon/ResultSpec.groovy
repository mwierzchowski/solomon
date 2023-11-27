package solomon

import spock.lang.Specification

class ResultSpec extends Specification {
    def result = new TestResult()

    def "Holds command value"() {
        given:
        def value = 1
        when:
        result.setValue(value)
        then:
        result.getValueOrThrowException() == value
        result.isSuccess()
    }

    def "Holds command null value"() {
        when:
        result.setValue(null)
        then:
        result.getValueOrThrowException() == null
        result.isSuccess()
    }

    def "Holds command exception"() {
        given:
        def exception = new IllegalArgumentException()
        when:
        result.setException(exception)
        result.getValueOrThrowException()
        then:
        Exception ex = thrown()
        ex == exception
        result.isFailure()
    }

    def "Provides value for successful result"() {
        given:
        def value = new Object()
        result.setValue(value)
        expect:
        result.getValue() == value
    }

    def "Provides exception for failed result"() {
        given:
        def exception = new RuntimeException()
        result.setException(exception)
        expect:
        result.getException() == exception
    }

    def "Changes exception to value"() {
        given:
        result.setException(new RuntimeException())
        when:
        result.setValue(1)
        then:
        result.isSuccess()
    }

    def "Changes value to exception"() {
        given:
        result.setValue(1)
        when:
        result.setException(new RuntimeException())
        then:
        result.isFailure()
    }

    static class TestResult implements Result<Object> {
        Object resultObject
    }
}