package solomonx

import spock.lang.Title

@Title("ConfigBuilder unregistered")
class ConfigBuilderDecorateSpec extends ConfigBuilderBaseSpec {
    def "decorates before"() {
        when:
        builder.applyBeforeDecorator(context -> {})
        then:
        isRegistered solomonx.api.Decorator
    }

    def "decorates after"() {
        when:
        builder.applyAfterDecorator(context -> {})
        then:
        isRegistered solomonx.api.Decorator
    }

    def "observes success"() {
        when:
        builder.applyOnSuccessObserver((command, value) -> {})
        then:
        isRegistered solomonx.api.Observer
    }

    def "observes failure"() {
        when:
        builder.applyOnFailureObserver((command, value) -> {})
        then:
        isRegistered solomonx.api.Observer
    }

    def isRegistered(Class<?> clazz) {
        return builder.addonDataMap.size() == 1
                && builder.addonDataMap.values()[0].applied
                && clazz.isInstance(builder.addonDataMap.values()[0].instance)
    }
}