package solomonx

import solomonx.spi.AddonCustomizer
import spock.lang.Title

@Title("ConfigBuilder customize")
class ConfigBuilderCustomizeSpec extends ConfigBuilderBaseSpec {
    def customization = data -> {
        data.setApplied(true)
    }

    def setup() {
        builder.register([decorator1, decorator2])
    }

    def "customizes with anonymous customizer"() {
        when:
        builder.customize(Decorator1, customization)
        then:
        builder.addonDataMap[Decorator1].applied
        !builder.addonDataMap[Decorator2].applied
    }

    def "customizes with defined customizer"() {
        given:
        def customizer = new AddonCustomizer(Decorator2, customization)
        when:
        builder.customize(customizer)
        then:
        !builder.addonDataMap[Decorator1].applied
        builder.addonDataMap[Decorator2].applied
    }

    def "customizes with list of defined customizers"() {
        given:
        def customizer1 = new AddonCustomizer(Decorator1, customization)
        def customizer2 = new AddonCustomizer(Decorator2, customization)
        when:
        builder.customize([customizer1, customizer2])
        then:
        builder.addonDataMap[Decorator1].applied
        builder.addonDataMap[Decorator2].applied
    }
}