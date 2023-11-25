package solomon.support;

import java.text.MessageFormat;

public class Utils {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object object) {
        return (T) object;
    }

    public static <T> T newInstanceOf(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            var msg = MessageFormat.format("Could not instantiate {0}", clazz);
            throw new IllegalArgumentException(msg, ex);
        }
    }
}
