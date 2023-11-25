package solomon.spi

import spock.lang.Specification

class DecoratorSpec extends Specification {
    def decorator = Mock(Decorator)

    def "Before throws exception"() {
        given:
        decorator.before(_) >> {
            throw new IllegalArgumentException()
        }
        when:
        decorator.before(null)
        then:
        thrown IllegalArgumentException
    }

    def "Safe after does not throw exception"() {
        given:
        decorator.after(_, _) >> {
            throw new IllegalArgumentException()
        }
        when:
        decorator.safeAfter(null, null)
        then:
        noExceptionThrown()
    }
}