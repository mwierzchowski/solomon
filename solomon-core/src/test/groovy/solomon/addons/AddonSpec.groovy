package solomon.addons

import solomon.annotations.Addon
import spock.lang.Specification

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

    @Addon(useCache = false)
    static class NonCacheableAddon extends DecoratorAdapter<Object, Object> {}
}