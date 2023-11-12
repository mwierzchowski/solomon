package solomon.utils

import spock.lang.Specification

class IterablesSpec extends Specification {
    def "Joins lists"() {
        given:
        def testIndex = 0
        expect:
        for (int item : Iterables.join(list1, list2, list3)) {
            assert item == joined[testIndex++]
        }
        assert testIndex == joined.size()
        where:
        list1     | list2     | list3     | joined
        [1, 2, 3] | [4, 5]    | [6]       | list1 + list2 + list3
        [1, 2, 3] | [4, 5]    | []        | list1 + list2 + list3
        [1, 2, 3] | []        | [4,5]     | list1 + list2 + list3
        [1, 2, 3] | []        | [4]       | list1 + list2 + list3
        [1, 2, 3] | []        | []        | list1 + list2 + list3
        []        | []        | []        | list1 + list2 + list3
        []        | [1, 2]    | [3]       | list1 + list2 + list3
        []        | []        | [1]       | list1 + list2 + list3
        [1]       | [2]       | [3]       | list1 + list2 + list3
        []        | [1]       | []        | list1 + list2 + list3
        [1, 2]    | null      | [3]       | list1 + list3
        null      | [1, 2]    | [3]       | list2 + list3
        null      | [1, 2]    | null      | list2
        null      | null      | [1]       | list3
        null      | null      | null      | []
    }

    def "Joins lists in reverse order"() {
        given:
        def testIndex = 0
        expect:
        for (int item : Iterables.joinReverse(list1, list2, list3)) {
            assert item == reversed[testIndex++]
        }
        assert testIndex == reversed.size()
        where:
        list1     | list2     | list3     | reversed
        [1, 2, 3] | [4, 5]    | [6]       | (list1 + list2 + list3).reverse()
        [1, 2, 3] | [4, 5]    | []        | (list1 + list2 + list3).reverse()
        [1, 2, 3] | []        | [4,5]     | (list1 + list2 + list3).reverse()
        [1, 2, 3] | []        | [4]       | (list1 + list2 + list3).reverse()
        [1, 2, 3] | []        | []        | (list1 + list2 + list3).reverse()
        []        | []        | []        | (list1 + list2 + list3).reverse()
        []        | [1, 2]    | [3]       | (list1 + list2 + list3).reverse()
        []        | []        | [1]       | (list1 + list2 + list3).reverse()
        [1]       | [2]       | [3]       | (list1 + list2 + list3).reverse()
        []        | [1]       | []        | (list1 + list2 + list3).reverse()
        [1, 2]    | null      | [3]       | (list1 + list3).reverse()
        null      | [1, 2]    | [3]       | (list2 + list3).reverse()
        null      | [1, 2]    | null      | list2.reverse()
        null      | null      | [1]       | list3.reverse()
        null      | null      | null      | []
    }
}