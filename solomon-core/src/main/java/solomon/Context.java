package solomon;

import java.util.Map;

import static solomon.Utils.cast;

public interface Context<C> extends CommandAware<C> {
    Map<Object, Object> getContextData(boolean forUpdate);

    default <T> Context<T> asContext() {
        return cast(this);
    }
}
