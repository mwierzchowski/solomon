package solomon

import groovy.transform.TupleConstructor
import solomon.addons.Addon
import solomon.addons.Decorator
import solomon.addons.DecoratorAdapter
import solomon.helpers.TestRunnableCmdDecorator
import solomon.Config
import spock.lang.Specification

class ConfigSpec extends Specification {
    def "Creates config with addons"() {
        given:
        def addons = [new AnyDecorator(1), new AnyDecorator(2)]
        when:
        def config = new Config(addons)
        then:
        config.count(Decorator) == 2
    }

    def "Adds addon to config"() {
        given:
        def config = new Config()
        when:
        config.add(new AnyDecorator(1))
        then:
        noExceptionThrown()
    }

    def "Throws ISE on add when config is locked"() {
        given:
        def config = new Config()
        config.lock()
        when:
        config.add(new AnyDecorator(1))
        then:
        thrown IllegalStateException
    }

    def "Unlocks creates new unlocked config when already locked"() {
        given:
        def config1 = new Config()
        config1.lock()
        when:
        def config2 = config1.unlock()
        config2.add(new AnyDecorator(1))
        then:
        noExceptionThrown()
        config2 != config1
    }

    def "Unlock does not create new config when it is not locked"() {
        given:
        def config1 = new Config()
        when:
        def config2 = config1.unlock()
        config2.add(new AnyDecorator(1))
        then:
        noExceptionThrown()
        config2 == config1
    }

    def "Throws IAE on add when addon type is not supported"() {
        given:
        def config = new Config()
        when:
        config.add(new FakeAddon())
        then:
        thrown IllegalArgumentException
    }

    def "Provides addon on given position"() {
        given:
        def config1 = new Config()
        def config2 = new Config(config1)
        config1.add(new AnyDecorator(1))
        config2.add(new AnyDecorator(2))
        expect:
        config2.get(Decorator, position).asType(AnyDecorator).id == id
        where:
        position | id
        0        | 1
        1        | 2
    }

    def "Throws IOBE on get when position is not valid"() {
        given:
        def config = new Config()
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
        def config = new Config()
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
        Config parent, child = null
        if (parentDecorators != null) {
            parent = new Config()
            parentDecorators.forEach {
                parent.add(new AnyDecorator(it))
            }
        }
        child = new Config((Config)parent)
        if (childDecorators != null) {
            childDecorators.forEach {
                child.add(new AnyDecorator(it))
            }
        }
        return child;
    }
}