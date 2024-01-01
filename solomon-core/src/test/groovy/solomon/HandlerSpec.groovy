package solomon

import spock.lang.Specification

import java.util.function.Supplier

class HandlerSpec extends Specification {
    def result = Mock(MutableResult)

    def "Runnable handler calls run and returns command itself"() {
        given:
        def command = Mock(Runnable)
        when:
        Handler.RUNNABLE.accept(command, result)
        then:
        1 * command.run()
        1 * result.setValue(_)
    }

    def "Supplier handler calls get and returns received value"() {
        given:
        def resultObject = new Object()
        def command = Mock(Supplier)
        when:
        Handler.SUPPLIER.accept(command, result)
        then:
        1 * command.get() >> resultObject
        1 * result.setValue(_)
    }
}