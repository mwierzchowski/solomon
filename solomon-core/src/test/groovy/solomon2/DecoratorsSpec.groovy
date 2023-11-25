package solomon2

import solomon2.core.Result
import solomon2.core.TestContext
import solomon2.support.Decorators
import spock.lang.Specification

import java.util.function.BiConsumer
import java.util.function.Consumer

class DecoratorsSpec extends Specification {
    def context = new TestContext()
    def result = new Result(new Object())

    def "Delegates before method call to handler"() {
        given:
        def handler = Mock(Consumer)
        def decorator = Decorators.before(handler)
        when:
        decorator.before(context)
        decorator.after(context, result)
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
        decorator.before(context)
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