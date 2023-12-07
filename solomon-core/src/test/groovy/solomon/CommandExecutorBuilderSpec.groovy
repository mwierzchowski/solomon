package solomon

import solomon.addons.Decorator
import solomon.addons.DecoratorAdapter
import solomon.addons.DecoratorSpec
import solomon.services.Factory
import solomon.addons.Observer
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

    def "Builds with configuration"() {
        given:
        def config = new Config()
        when:
        def executor = builder.withConfig(config).build()
        then:
        executor.globalConfig == config
    }

    def "Builds with global addon instance"() {
        given:
        def decorator = Mock(Decorator)
        def observer = Mock(Observer)
        when:
        def executor = builder.withGlobalAddon(decorator)
                .withGlobalAddon(observer)
                .build()
        then:
        executor.globalConfig.get(Decorator, 0) == decorator
        executor.globalConfig.get(Observer, 0) == observer
    }

    def "Builds with global addon class"() {
        when:
        def executor = builder.withGlobalAddon(TestDecorator).build()
        then:
        executor.globalConfig.get(Decorator, 0).getClass() == TestDecorator

    }

    def "Builds with cached addons"() {
        given:
        def decorator = Mock(Decorator)
        def observer = Mock(Observer)
        when:
        def executor = builder.withCachedAddon(decorator)
                .withCachedAddon(observer)
                .build()
        then:
        executor.factory.getInstanceOf(decorator.getClass()) == decorator
        executor.factory.getInstanceOf(observer.getClass()) == observer
    }

    static class TestDecorator extends DecoratorAdapter<Object, Object> {}
}