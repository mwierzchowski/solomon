package solomon.spi

import spock.lang.Specification

class ListenerSpec extends Specification {
    def listener = Mock(Listener)

    def "Safe onSuccess does not throw exception"() {
        given:
        listener.onSuccess(_, _) >> {
            throw new IllegalArgumentException()
        }
        when:
        listener.safeOnSuccess(null, null)
        then:
        noExceptionThrown()
    }

    def "Safe onError does not throw exception"() {
        given:
        listener.onFailure(_, _) >> {
            throw new IllegalArgumentException()
        }
        when:
        listener.safeOnFailure(null, null)
        then:
        noExceptionThrown()
    }
}