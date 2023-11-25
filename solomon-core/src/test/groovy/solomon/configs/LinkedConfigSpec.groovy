package solomon.configs

import groovy.transform.TupleConstructor
import solomon.helpers.TestRunnableCmdDecorator
import solomon.spi.Addon
import solomon.spi.Decorator
import solomon.support.DecoratorAdapter
import spock.lang.Specification

class LinkedConfigSpec extends Specification {
    def "Adds addon to config"() {
        given:
        def config = new LinkedConfig(null)
        when:
        config.add(new AnyDecorator(1))
        then:
        noExceptionThrown()
    }

    def "Throws IAE on add when addon type is not supported"() {
        given:
        def config = new LinkedConfig(null)
        when:
        config.add(new FakeAddon())
        then:
        thrown IllegalArgumentException
    }

    def "Provides addon on given position"() {
        given:
        def config1 = new LinkedConfig(null)
        def config2 = new LinkedConfig(config1)
        config1 = config1.add(new AnyDecorator(1))
        config2 = config2.add(new AnyDecorator(2))
        expect:
        config2.get(Decorator, position).asType(AnyDecorator).id == id
        where:
        position | id
        0        | 1
        1        | 2
    }

    def "Throws IOBE on get when position is not valid"() {
        given:
        def config = new LinkedConfig(null)
        config.add(new TestRunnableCmdDecorator())
        when:
        config.get(Decorator, index)
        then:
        thrown IndexOutOfBoundsException
        where:
        index << [-1, 1]
    }

    def "Throws IAE on get when addon type is not supported"() {
        given:
        def config = new LinkedConfig(null)
        config.add(new TestRunnableCmdDecorator())
        when:
        config.get(FakeAddon, 0)
        then:
        thrown IllegalArgumentException
    }

    def "Counts addons"() {
        given:
        def config = buildConfig(parent, child)
        expect:
        config.count(Decorator) == count
        where:
        parent    | child     | count
        null      | []        | 0
        null      | [1, 2]    | 2
        [1, 2, 3] | []        | 3
        [1, 2]    | [3, 4, 5] | 5
    }

    def "Provides new config linked to this one"() {
        given:
        def config1 = new LinkedConfig(null)
        when:
        def config2 = config1.chain()
        then:
        config2 != config1
        config2.class == LinkedConfig
        config2.parent == config1
    }

    def "Checks if addon is in the config"() {
        given:
        def config = buildConfig([0, 1], [2, 3, 4])
        expect:
        config.contains(Decorator, position) == contains
        where:
        position | contains
        -1       | false
         0       | true
         1       | true
         4       | true
         5       | false
    }

    static class FakeAddon implements Addon {}

    @TupleConstructor(includeFields = true)
    static class AnyDecorator extends DecoratorAdapter<Object, Object> {
        int id
    }

    Config buildConfig(List<Integer> parentDecorators, List<Integer> childDecorators) {
        LinkedConfig parent, child = null;
        if (parentDecorators != null) {
            parent = new LinkedConfig(null)
            parentDecorators.forEach {
                parent.add(new AnyDecorator(it))
            }
        }
        child = new LinkedConfig(parent)
        if (childDecorators != null) {
            childDecorators.forEach {
                child.add(new AnyDecorator(it))
            }
        }
        return child;
    }
}