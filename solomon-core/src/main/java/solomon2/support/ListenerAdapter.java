package solomon2.support;

import solomon2.spi.Listener;

public abstract class ListenerAdapter<C, V> implements Listener<C, V> {
    @Override
    public void onSuccess(C command, V value) {}

    @Override
    public void onFailure(C command, RuntimeException exception) {}
}
