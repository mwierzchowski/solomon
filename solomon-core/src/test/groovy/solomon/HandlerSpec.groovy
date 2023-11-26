package solomon

import spock.lang.Specification

import java.util.function.Supplier

class HandlerSpec extends Specification {
    def "Runnable handler calls run and returns command itself"() {
        given:
        def command = Mock(Runnable)
        when:
        def result = Handler.RUNNABLE.apply(command)
        then:
        1 * command.run()
        result != null
        result.getValue() == command
    }

    def "Supplier handler calls get and returns received value"() {
        given:
        def resultObject = new Object()
        def command = Mock(Supplier)
        when:
        def result = Handler.SUPPLIER.apply(command)
        then:
        1 * command.get() >> resultObject
        result != null
        result.getValue() == resultObject
    }
}