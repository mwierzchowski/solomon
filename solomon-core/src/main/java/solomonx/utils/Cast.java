package solomonx.utils;

public interface Cast {
    @SuppressWarnings("unchecked")
    static <T> T unchecked(Object object) {
        return (T) object;
    }
}
