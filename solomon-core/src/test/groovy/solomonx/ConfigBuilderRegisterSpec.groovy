package solomonx

import spock.lang.Title

@Title("ConfigBuilder registration")
class ConfigBuilderRegisterSpec extends ConfigBuilderBaseSpec {
    def "registers addon"() {
        when:
        builder.register(decorator1)
        then:
        builder.addonDataMap[Decorator1].instance == decorator1
    }

    def "registers addon list"() {
        when:
        builder.register([decorator1, decorator2])
        then:
        builder.addonDataMap[Decorator1].instance == decorator1
        builder.addonDataMap[Decorator2].instance == decorator2
    }

    def "registers addon data"() {
        when:
        builder.registerData(decoratorData1)
        then:
        builder.addonDataMap[Decorator1].instance == decorator1
    }

    def "registers addon data list"() {
        when:
        builder.registerData([decoratorData1, decoratorData2])
        then:
        builder.addonDataMap[Decorator1].instance == decorator1
        builder.addonDataMap[Decorator2].instance == decorator2
    }
}