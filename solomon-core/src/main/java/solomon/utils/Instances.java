package solomon.utils;

import lombok.NonNull;

import java.text.MessageFormat;

public interface Instances {
    static <C> C instantiate(@NonNull Class<C> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            var msg = MessageFormat.format("Could not instantiate {0}", clazz);
            throw new IllegalArgumentException(msg, ex);
        }
    }
}
