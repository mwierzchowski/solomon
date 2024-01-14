package solomonx

import spock.lang.Title

@Title("ConfigBuilder unregistered")
class ConfigBuilderDecorateSpec extends ConfigBuilderBaseSpec {
    def "decorate before"() {
        when:
        builder.decorateBefore(context -> {})
        then:
        isRegistered solomonx.api.Decorator
    }

    def "decorate after"() {
        when:
        builder.decorateAfter(context -> {})
        then:
        isRegistered solomonx.api.Decorator
    }

    def "observe success"() {
        when:
        builder.onSuccess((command, value) -> {})
        then:
        isRegistered solomonx.api.Observer
    }

    def "observe failure"() {
        when:
        builder.onFailure((command, value) -> {})
        then:
        isRegistered solomonx.api.Observer
    }

    def isRegistered(Class<?> clazz) {
        return builder.addonDataMap.size() == 1
                && builder.addonDataMap.values()[0].applied
                && clazz.isInstance(builder.addonDataMap.values()[0].instance)
    }
}