package solomon.core

import solomon.Result
import spock.lang.Specification

class ResultSpec extends Specification {
    def "Holds command value"() {
        given:
        def value = 1
        when:
        def result = new Result(value)
        then:
        result.getValueOrThrowException() == value
        result.isSuccess()
    }

    def "Holds command null value"() {
        when:
        def result = new Result(null)
        then:
        result.getValueOrThrowException() == null
        result.isSuccess()
    }

    def "Holds command exception"() {
        given:
        def exception = new IllegalArgumentException()
        when:
        def result = new Result(exception)
        result.getValueOrThrowException()
        then:
        Exception ex = thrown()
        ex == exception
        result.isFailure()
    }

    def "Throws NPE when exception is null"() {
        when:
        new Result((RuntimeException) null)
        then:
        thrown NullPointerException
    }

    def "Changes value to exception"() {
        given:
        def result = new Result(1)
        when:
        result.change(new RuntimeException())
        then:
        result.isFailure()
    }

    def "Changes exception to value"() {
        given:
        def result = new Result(new RuntimeException())
        when:
        result.change(1)
        then:
        result.isSuccess()
    }

    def "Provides value for successful result"() {
        given:
        def value = new Object()
        def result = new Result(value)
        expect:
        result.getValue() == value
    }

    def "Provides exception for failed result"() {
        given:
        def exception = new RuntimeException()
        def result = new Result(exception)
        expect:
        result.getException() == exception
    }

    def "Throws ISE when value is requested from failed result"() {
        given:
        def result = new Result(new RuntimeException())
        when:
        result.getValue()
        then:
        thrown IllegalStateException
    }

    def "Throws ISE when exception is requested from successful result"() {
        given:
        def result = new Result(new Object())
        when:
        result.getException()
        then:
        thrown IllegalStateException
    }
}