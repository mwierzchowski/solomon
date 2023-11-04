package solomon;

import java.text.MessageFormat;

public class CommandExecutorJdk extends CommandExecutor {
    @Override
    public <C> C instantiateCommand(Class<C> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception ex) {
            var msg = MessageFormat.format("Could not instantiate {0}", clazz);
            throw new IllegalArgumentException(msg, ex);
        }
    }
}
