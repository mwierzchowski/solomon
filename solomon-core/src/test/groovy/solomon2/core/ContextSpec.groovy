package solomon2.core


import spock.lang.Specification

class ContextSpec extends Specification {
    def context = new TestContext(new Object())

    def "Lazy initializes storage"() {
        expect:
        context.data == null
    }

    def "Stores object"() {
        given:
        def key = "test-key"
        def object = new Object()
        when:
        context.store(key, object)
        then:
        context.data.get(key) == object
    }

    def "Retrieves object"() {
        given:
        def key = "test-key"
        def object = new Object()
        context.store(key, object)
        expect:
        context.retrieve(key) == object
    }

    def "Retrieves and casts object"() {
        given:
        def key = "test-key"
        def object = Integer.valueOf(1)
        context.store(key, object)
        when:
        def retrieved = context.retrieve(key, Integer)
        then:
        retrieved == object
        retrieved instanceof Integer
    }

    def "Check if contains key"() {
        given:
        def key1 = "test-key-1"
        def key2 = "test-key-2"
        context.store(key1, new Object())
        expect:
        context.contains(key1)
        !context.contains(key2)
    }

    def "Storing throws NPE when key is null"() {
        when:
        context.store(null, null)
        then:
        thrown NullPointerException
    }
}