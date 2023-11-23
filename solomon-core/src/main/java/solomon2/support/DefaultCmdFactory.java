package solomon2.support;

import solomon2.spi.CmdFactory;

import java.text.MessageFormat;

public class DefaultCmdFactory implements CmdFactory {
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
