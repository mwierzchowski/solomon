package solomon.addons

import spock.lang.Specification

import java.util.function.BiConsumer

class ListenersSpec extends Specification {
    def command = new Object()
    def value = new Object()
    def exception = new RuntimeException()

    def "Delegates onSuccess method call to handler"() {
        given:
        def handler = Mock(BiConsumer)
        def listener = Listeners.onSuccess(handler)
        when:
        listener.onSuccess(command, value)
        then:
        1 * handler.accept(command, value)
    }

    def "Throws NPE when success handler is null"() {
        when:
        Listeners.onSuccess(null)
        then:
        thrown NullPointerException
    }

    def "Delegates onFailure method call to handler"() {
        given:
        def handler = Mock(BiConsumer)
        def listener = Listeners.onFailure(handler)
        when:
        listener.onFailure(command, exception)
        then:
        1 * handler.accept(command, exception)
    }

    def "Throws NPE when failure handler is null"() {
        when:
        Listeners.onFailure(null)
        then:
        thrown NullPointerException
    }
}