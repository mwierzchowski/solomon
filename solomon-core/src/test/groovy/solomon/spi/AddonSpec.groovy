package solomon.spi

import solomon.annotation.AddonConfig
import solomon.support.DecoratorAdapter
import spock.lang.Specification

import static solomon.annotation.AddonConfig.Scope.PROTOTYPE

class AddonSpec extends Specification {
    def "Addon is cacheable by default"() {
        given:
        def addon = new DefaultConfigAddon()
        expect:
        addon.isCacheable()
    }

    def "Addon might be configured to not be cacheable"() {
        given:
        def addon = new NonCacheableAddon()
        expect:
        !addon.isCacheable()
    }

    static class DefaultConfigAddon extends DecoratorAdapter<Object , Object> {}

    @AddonConfig(scope = PROTOTYPE)
    static class NonCacheableAddon extends DecoratorAdapter<Object, Object> {}
}