package solomon.services

import groovy.transform.RecordType
import solomon.Config
import solomon.addons.Decorator
import solomon.addons.DecoratorAdapter
import solomon.addons.Observer
import solomon.addons.ObserverAdapter
import solomon.annotations.Decorated
import solomon.annotations.Observed
import spock.lang.Specification

class DefaultProcessorSpec extends Specification {
    def processor = new DefaultProcessor()
    def globalConfig = new Config()
    def factory = Mock(Factory)
    def annotationMap = new Processor.AnnotationMap()
    def context = new TestContext(factory, globalConfig, annotationMap)

    def "Adds addons from annotation to the config"() {
        when:
        def processedConfig = processor.process(new DummyCommand1(), context)
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
        def config1 = processor.process(command, context)
        def config2 = processor.process(command, context)
        then:
        config1 == config2
    }

    def "Unlocks config before updating"() {
        given:
        globalConfig.lock()
        when:
        def processedConfig = processor.process(new DummyCommand1(), context)
        then:
        1 * factory.getInstanceOf(DummyDecorator) >> new DummyDecorator()
        1 * factory.getInstanceOf(DummyObserver) >> new DummyObserver()
        processedConfig.count(Decorator) == 1
        processedConfig.count(Observer) == 1
        processedConfig != globalConfig
        globalConfig.count(Decorator) == 0
        globalConfig.count(Observer) == 0
    }


    def "Does not add addons when annotation is empty or missing"() {
        when:
        def processedConfig = processor.process(command, context)
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

    @RecordType
    static class TestContext implements Processor.Context {
        Factory factory
        Config globalConfig
        Processor.AnnotationMap annotationMap
    }
}