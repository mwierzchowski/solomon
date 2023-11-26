package solomon

import solomon.addons.Decorator
import solomon.services.Factory
import solomon.addons.Listener
import spock.lang.Specification

class CommandExecutorBuilderSpec extends Specification {
    def builder = new CommandExecutorBuilder()

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

    def "Builds with global addons"() {
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

    def "Builds with registered addons"() {
        given:
        def decorator = Mock(Decorator)
        def listener = Mock(Listener)
        when:
        def executor = builder.withRegisteredAddon(decorator)
            .withRegisteredAddon(listener)
            .build()
        then:
        executor.factory.getInstanceOf(decorator.getClass()) == decorator
        executor.factory.getInstanceOf(listener.getClass()) == listener
    }
}