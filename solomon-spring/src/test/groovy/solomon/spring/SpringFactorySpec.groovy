package solomon.spring

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.ApplicationContext
import solomon.addons.Addon
import solomon.services.Factory
import spock.lang.Specification

class SpringFactorySpec extends Specification {
    def nonSpringFactory = Mock(Factory)
    def springContext = Mock(ApplicationContext)
    def springFactory = new SpringFactory(nonSpringFactory, springContext)

    def "Uses application context to instantiate bean"() {
        given:
        def object1 = new Object()
        when:
        def object2 = springFactory.getInstanceOf(Object)
        then:
        1 * springContext.getBean(Object) >> object1
        object2 == object1
    }

    def "Falls back to non-spring factory when spring initialization fails"() {
        given:
        def object1 = new Object()
        when:
        def object2 = springFactory.getInstanceOf(Object)
        then:
        1 * springContext.getBean(Object) >> {
            throw new NoSuchBeanDefinitionException(Object)
        }
        1 * nonSpringFactory.getInstanceOf(Object) >> object1
        object2 == object1
    }

    def "Caches beans only for non-spring factory"() {
        given:
        def addon = new DummyAddon()
        when:
        springFactory.cache(addon)
        then:
        1 * nonSpringFactory.cache(addon)
    }

    static class DummyAddon implements Addon {}
}