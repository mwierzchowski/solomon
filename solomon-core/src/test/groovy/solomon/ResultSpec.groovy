package solomon

import spock.lang.Specification

class ResultSpec extends Specification {
    def result = new TestResult()

    def "Holds command value"() {
        given:
        def value = 1
        when:
        result.setSuccess(value)
        then:
        result.getValueOrThrowException() == value
        result.isSuccess()
    }

    def "Holds command null value"() {
        when:
        result.setSuccess(null)
        then:
        result.getValueOrThrowException() == null
        result.isSuccess()
    }

    def "Holds command exception"() {
        given:
        def exception = new IllegalArgumentException()
        when:
        result.setFailure(exception)
        result.getValueOrThrowException()
        then:
        Exception ex = thrown()
        ex == exception
        result.isFailure()
    }

    def "Provides value for successful result"() {
        given:
        def value = new Object()
        result.setSuccess(value)
        expect:
        result.getValue() == value
    }

    def "Provides exception for failed result"() {
        given:
        def exception = new RuntimeException()
        result.setFailure(exception)
        expect:
        result.getException() == exception
    }

    def "Changes exception to value"() {
        given:
        result.setFailure(new RuntimeException())
        when:
        result.setSuccess(1)
        then:
        result.isSuccess()
    }

    def "Changes value to exception"() {
        given:
        result.setSuccess(1)
        when:
        result.setException(new RuntimeException())
        then:
        result.isFailure()
    }

    static class TestResult implements Result<Object> {
        Object value
        RuntimeException exception
    }
}