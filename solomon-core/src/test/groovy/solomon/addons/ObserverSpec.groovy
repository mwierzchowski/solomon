package solomon.addons

import spock.lang.Specification

class ObserverSpec extends Specification {
    def observer = Mock(Observer)

    def "Safe onSuccess does not throw exception"() {
        given:
        observer.onSuccess(_, _) >> {
            throw new IllegalArgumentException()
        }
        when:
        observer.safeOnSuccess(null, null)
        then:
        noExceptionThrown()
    }

    def "Safe onError does not throw exception"() {
        given:
        observer.onFailure(_, _) >> {
            throw new IllegalArgumentException()
        }
        when:
        observer.safeOnFailure(null, null)
        then:
        noExceptionThrown()
    }
}