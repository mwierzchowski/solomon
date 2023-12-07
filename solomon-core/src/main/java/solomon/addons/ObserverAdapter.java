package solomon.addons;

public abstract class ObserverAdapter<C, V> implements Observer<C, V> {
    @Override
    public void onSuccess(C command, V value) {}

    @Override
    public void onFailure(C command, RuntimeException exception) {}
}
