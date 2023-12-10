package solomon.spring.factory

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import solomon.addons.Addon
import solomon.services.Factory
import spock.lang.Specification

class ApplicationContextAdapterSpec extends Specification {
    def applicationContext = Mock(ApplicationContext)
    def fallbackFactory = Mock(Factory)
    def adapter = new ApplicationContextAdapter(applicationContext, fallbackFactory)

    def "Uses application context to instantiate bean"() {
        given:
        def object1 = new Object()
        when:
        def object2 = adapter.getInstanceOf(Object)
        then:
        1 * applicationContext.getBean(Object) >> object1
        object2 == object1
    }

    def "Falls back to non-spring factory when spring initialization fails"() {
        given:
        def object1 = new Object()
        when:
        def object2 = adapter.getInstanceOf(Object)
        then:
        1 * applicationContext.getBean(Object) >> {
            throw new NoSuchBeanDefinitionException(Object)
        }
        1 * fallbackFactory.getInstanceOf(Object) >> object1
        object2 == object1
    }

    def "Caches beans only for non-spring factory"() {
        given:
        def addon = new DummyAddon()
        when:
        adapter.cache(addon)
        then:
        1 * fallbackFactory.cache(addon)
    }

    static class DummyAddon implements Addon {}
}