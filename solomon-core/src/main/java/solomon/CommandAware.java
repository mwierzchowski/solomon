package solomon;

public interface CommandAware<C> {
    C getCommand();
}
