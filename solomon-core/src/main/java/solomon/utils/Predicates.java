package solomon.utils;

import java.util.Objects;
import java.util.function.Predicate;

public interface Predicates {
    static <C> Predicate<C> predicateBy(Class<? extends C> clazz) {
        return object -> Objects.equals(object.getClass(), clazz);
    }
}
