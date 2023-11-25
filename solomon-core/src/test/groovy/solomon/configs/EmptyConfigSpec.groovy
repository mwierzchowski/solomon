package solomon.configs

import solomon.helpers.TestRunnableCmdDecorator
import solomon.spi.Decorator
import solomon.spi.Listener
import spock.lang.Specification

class EmptyConfigSpec extends Specification {
    def config = EmptyConfig.INSTANCE

    def "Returns other non-empty config on add"() {
        when:
        def config2 = config.add(new TestRunnableCmdDecorator())
        then:
        config2 != null
        config2 != config
        config2.class != EmptyConfig
    }

    def "Always throws IOBE on get"() {
        when:
        config.get(type, 0)
        then:
        thrown IndexOutOfBoundsException
        where:
        type << [Decorator, Listener]
    }

    def "Always returns 0 on size"() {
        expect:
        config.count(type) == 0
        where:
        type << [Decorator, Listener]
    }

    def "Always returns itself on chain"() {
        expect:
        config.chain() == config
    }

    def "Always returns false on contains"() {
        expect:
        !config.contains(type, position)
        where:
        type      |  position
        Decorator |  0
        Decorator |  1
        Decorator | -1
        Listener  |  0
        Listener  |  1
        Listener  | -1
    }
}