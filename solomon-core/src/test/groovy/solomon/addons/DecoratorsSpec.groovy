package solomon.addons

import solomon.ExecutionContext
import solomon.Result
import spock.lang.Specification

import java.util.function.BiConsumer
import java.util.function.Consumer

class DecoratorsSpec extends Specification {
    def context = Mock(ExecutionContext)
    def result = Mock(Result)

    def "Delegates before method call to handler"() {
        given:
        def handler = Mock(Consumer)
        def decorator = Decorators.before(handler)
        when:
        decorator.before(context)
        then:
        1 * handler.accept(context)
    }

    def "Throws NPE when before handler is null"() {
        when:
        Decorators.before(null)
        then:
        thrown NullPointerException
    }

    def "Delegates after method call to handler"() {
        given:
        def handler = Mock(BiConsumer)
        def decorator = Decorators.after(handler)
        when:
        decorator.after(context, result)
        then:
        1 * handler.accept(context, result)
    }

    def "Throws NPE when after handler is null"() {
        when:
        Decorators.after(null)
        then:
        thrown NullPointerException
    }
}