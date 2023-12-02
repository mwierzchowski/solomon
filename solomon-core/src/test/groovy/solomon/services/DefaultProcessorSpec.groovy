package solomon.services

import solomon.Config
import solomon.addons.Decorator
import solomon.addons.DecoratorAdapter
import solomon.addons.Listener
import solomon.addons.ListenerAdapter
import solomon.annotations.Command
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
        1 * factory.getInstanceOf(DummyListener) >> new DummyListener()
        processedConfig.count(Decorator) == 1
        processedConfig.count(Listener) == 1
    }

    def "Unlocks config before updating"() {
        given:
        config.lock()
        when:
        def processedConfig = processor.process(new DummyCommand1(), config)
        then:
        1 * factory.getInstanceOf(DummyDecorator) >> new DummyDecorator()
        1 * factory.getInstanceOf(DummyListener) >> new DummyListener()
        processedConfig.count(Decorator) == 1
        processedConfig.count(Listener) == 1
        processedConfig != config
        config.count(Decorator) == 0
        config.count(Listener) == 0
    }


    def "Does not add addons when annotation is empty or missing"() {
        when:
        def processedConfig = processor.process(command, config)
        then:
        0 * factory.getInstanceOf(_)
        processedConfig.count(Decorator) == 0
        processedConfig.count(Listener) == 0
        where:
        command << [new DummyCommand2(), new DummyCommand3()]
    }

    static class DummyDecorator extends DecoratorAdapter<Object, Object> {}
    static class DummyListener extends ListenerAdapter<Object, Object> {}

    @Command(decorators = [DummyDecorator], listeners = [DummyListener])
    static class DummyCommand1 {}

    @Command
    static class DummyCommand2 {}

    static class DummyCommand3 {}
}