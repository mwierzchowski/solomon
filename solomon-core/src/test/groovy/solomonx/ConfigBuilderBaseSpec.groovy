package solomonx

import solomonx.spi.AddonData
import solomonx.spi.AddonProcessor
import solomonx.support.DecoratorAdapter
import spock.lang.Shared
import spock.lang.Specification

class ConfigBuilderBaseSpec extends Specification {
    @Shared def decorator1 = new Decorator1()
    @Shared def decorator2 = new Decorator2()
    @Shared def decoratorData1 = new AddonData(decorator1)
    @Shared def decoratorData2 = new AddonData(decorator2)
    def addonProcessor = Mock(AddonProcessor)
    def builder = new ConfigBuilder([addonProcessor])

    static class Decorator1 extends DecoratorAdapter<Object, Object> {}
    static class Decorator2 extends DecoratorAdapter<Object, Object> {}
}
