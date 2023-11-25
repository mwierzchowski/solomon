package solomon2.support

import spock.lang.Specification

class DefaultFactorySpec extends Specification {
    def factory = new DefaultCommandFactory()

    def "Instantiates class with no arguments constructor"() {
        when:
        def object = factory.instantiate(Object)
        then:
        object != null
    }

    def "Throws RuntimeException when instantiation is not possible"() {
        when:
        factory.instantiate(Void)
        then:
        thrown RuntimeException
    }
}