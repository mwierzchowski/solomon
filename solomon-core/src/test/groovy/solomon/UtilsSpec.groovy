package solomon

import spock.lang.Specification

class UtilsSpec extends Specification {
    def "Casts object to target class"() {
        given:
        List<?> someList = Arrays.asList(0, 1, 2);
        when:
        List<Integer> integerList = Utils.cast(someList);
        then:
        integerList != null
        for (int i = 0; i < 3; i++) {
            assert integerList[i] == i
        }
    }

    def "Instantiates class with no arguments constructor"() {
        when:
        def object = Utils.newInstanceOf(Object)
        then:
        object != null
    }

    def "Throws RuntimeException when instantiation is not possible"() {
        when:
        Utils.newInstanceOf(Void)
        then:
        thrown RuntimeException
    }
}