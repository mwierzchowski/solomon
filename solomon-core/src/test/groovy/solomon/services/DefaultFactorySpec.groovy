package solomon.services

import solomon.addons.DecoratorAdapter
import solomon.annotations.AddonConfig
import spock.lang.Specification

import static solomon.annotations.AddonConfig.Scope.PROTOTYPE
import static solomon.annotations.AddonConfig.Scope.SERVICE

class DefaultFactorySpec extends Specification {
    def factory = new DefaultFactory()

    def "Creates new instances for non-addons"() {
        when:
        def object1 = factory.getInstanceOf(Object)
        def object2 = factory.getInstanceOf(Object)
        then:
        object1 != null
        object2 != null
        object1 != object2
    }

    def "Creates new instances for non cacheable addons"() {
        when:
        def object1 = factory.getInstanceOf(NonCacheableAddon)
        def object2 = factory.getInstanceOf(NonCacheableAddon)
        then:
        object1 != null
        object2 != null
        object1 != object2
    }

    def "Creates and caches instances for cacheable addons"() {
        when:
        def object1 = factory.getInstanceOf(CacheableAddon)
        def object2 = factory.getInstanceOf(CacheableAddon)
        then:
        object1 != null
        object2 != null
        object1 == object2
    }

    def "Caches registered instances of addons"() {
        given:
        def addon1 = new NonCacheableAddon()
        factory.register(addon1)
        when:
        def addon2 = factory.getInstanceOf(NonCacheableAddon)
        then:
        addon2 != null
        addon2 == addon1
    }

    @AddonConfig(scope = SERVICE)
    static class CacheableAddon extends DecoratorAdapter<Object, Object> {}

    @AddonConfig(scope = PROTOTYPE)
    static class NonCacheableAddon extends DecoratorAdapter<Object, Object> {}
}