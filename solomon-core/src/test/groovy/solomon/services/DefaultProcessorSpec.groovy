package solomon.services

import solomon.Config
import solomon.addons.Decorator
import solomon.addons.DecoratorAdapter
import solomon.addons.Observer
import solomon.addons.ObserverAdapter
import solomon.annotations.Decorated
import solomon.annotations.Observed
import spock.lang.Specification

class DefaultProcessorSpec extends Specification {
    def factory = Mock(Factory)
    def processor = new DefaultProcessor(factory)
    def config = new Config()

    def "Adds addons from annotation to the config"() {
        when:
        def processedConfig = processor.process(new DummyCommand1(), config)
        then:
        1 * factory.getInstanceOf(DummyDecorator) >> new DummyDecorator()
        1 * factory.getInstanceOf(DummyObserver) >> new DummyObserver()
        processedConfig.count(Decorator) == 1
        processedConfig.count(Observer) == 1
    }

    def "Caches config read from annotation"() {
        given:
        factory.getInstanceOf(DummyDecorator) >> new DummyDecorator()
        factory.getInstanceOf(DummyObserver) >> new DummyObserver()
        def command = new DummyCommand1()
        when:
        def config1 = processor.process(command, config)
        def config2 = processor.process(command, config)
        then:
        config1 == config2
    }

    def "Unlocks config before updating"() {
        given:
        config.lock()
        when:
        def processedConfig = processor.process(new DummyCommand1(), config)
        then:
        1 * factory.getInstanceOf(DummyDecorator) >> new DummyDecorator()
        1 * factory.getInstanceOf(DummyObserver) >> new DummyObserver()
        processedConfig.count(Decorator) == 1
        processedConfig.count(Observer) == 1
        processedConfig != config
        config.count(Decorator) == 0
        config.count(Observer) == 0
    }


    def "Does not add addons when annotation is empty or missing"() {
        when:
        def processedConfig = processor.process(command, config)
        then:
        0 * factory.getInstanceOf(_)
        processedConfig.count(Decorator) == 0
        processedConfig.count(Observer) == 0
        where:
        command << [new DummyCommand2(), new DummyCommand3()]
    }

    static class DummyDecorator extends DecoratorAdapter<Object, Object> {}
    static class DummyObserver extends ObserverAdapter<Object, Object> {}

    @Decorated(DummyDecorator)
    @Observed(DummyObserver)
    static class DummyCommand1 {}

    static class DummyCommand2 {}

    static class DummyCommand3 {}
}