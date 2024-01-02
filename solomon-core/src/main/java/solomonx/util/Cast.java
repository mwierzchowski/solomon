package solomonx.util;

public interface Cast {
    @SuppressWarnings("unchecked")
    static <T> T unchecked(Object object) {
        return (T) object;
    }
}
