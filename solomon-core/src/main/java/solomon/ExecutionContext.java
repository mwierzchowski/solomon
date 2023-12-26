package solomon;

import java.util.Map;

import static solomon.Utils.cast;

public interface ExecutionContext<C> extends CommandAware<C> {
    Map<Object, Object> getContextData(boolean forUpdate);

    static <T> ExecutionContext<T> asContext(ExecutionContext<?> context) {
        return cast(context);
    }
}
