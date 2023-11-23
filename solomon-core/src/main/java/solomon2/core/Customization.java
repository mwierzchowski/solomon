package solomon2.core;

import lombok.NonNull;
import solomon2.spi.Decorator;
import solomon2.spi.Listener;

public interface Customization {
    Customization addDecorator(@NonNull Decorator<?, ?> decorator);
    Customization addListener(@NonNull Listener<?, ?> listener);
}
