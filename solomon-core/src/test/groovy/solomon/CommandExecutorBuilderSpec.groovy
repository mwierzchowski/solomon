package solomon

import solomon.spi.Decorator
import solomon.spi.Factory
import solomon.spi.Listener
import spock.lang.Specification

class CommandExecutorBuilderSpec extends Specification {
    def builder = new CommandExecutor.Builder()

    def "Builds with defaults"() {
        when:
        def executor = builder.build()
        then:
        executor.factory != null
        executor.processor != null
        executor.globalConfig != null
    }

    def "Builds with factory"() {
        given:
        def factory = Mock(Factory)
        when:
        def executor = builder.withFactory(factory).build()
        then:
        executor.factory == factory
    }

    def "Builds with addons"() {
        given:
        def decorator = Mock(Decorator)
        def listener = Mock(Listener)
        when:
        def executor = builder.withGlobalAddon(decorator)
                .withGlobalAddon(listener)
                .build()
        then:
        executor.globalConfig.get(Decorator, 0) == decorator
        executor.globalConfig.get(Listener, 0) == listener
    }
}