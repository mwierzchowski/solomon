package solomon2.support;

import solomon2.spi.CommandFactory;

import java.text.MessageFormat;

public class DefaultCommandFactory implements CommandFactory {
    @Override
    public <C> C instantiate(Class<C> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            var msg = MessageFormat.format("Could not instantiate {0}", clazz);
            throw new IllegalArgumentException(msg, ex);
        }
    }
}
