package solomon.flows;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 TODO
 - callback do zwracania metryki czasu wykonania
 - a moze runnable onSuccess moze przesylac zwykly obiekt null bez pakowania w Optional?
 - moze runnable powinno wysylac onSuccess resultat jako czas wykonania?
 - jesli zdecyduje sie na powyzsze to Supplier powinien wysylac resultat i rowniez czas wykonania
 - czy decoratory powinny byc uzywane wewnetrznie? chyba nie, to zabiera czas procesora i sterte
 - moze powinienem wystawiac jakies metryki typu ile jest dekoratorow?
 - komendy Runnable moga zwracac na execute sama siebie, dzieki czemu moga zwracac wiele resultatow, byc moze dzieki temu
   uda sie tez uwspolnic interfejs z komendami supplier

 OGOLNIE, przemyslec co mogloby byc dekoratorem (w springboot) a co mogloby sluchac na onSuccess. Czy naprawde chcialbym
 cos tam podlaczac w rzeczywistym projekcie? Mam poczucie ze pewnie niczego bym tam produkcyjnie nie wrzucal i dlatego
 zastanawiam sie czy decoratory nie powinny byc uzywane wewnetrznie - to by uratowalo kod ktory z takim wysilkiem napisalem :)
 */

@Slf4j
public abstract class CommandFlowBase<F, C extends I, I, R> {
    protected final C command;
    protected I callStack;
    protected boolean initialized;
    protected List<Consumer<Optional<R>>> successListeners;
    protected List<Consumer<RuntimeException>> failureListeners;

    protected CommandFlowBase(@NonNull C command) {
        this.command = command;
        this.callStack = command;
        this.initialized = false;
    }

    @SuppressWarnings("unchecked")
    public F initialize(@NonNull Consumer<C> initializer) {
        if (this.initialized) {
            LOG.warn("Command {} already initialized", cmdName());
        }
        initializer.accept(this.command);
        this.initialized = true;
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F decorate(@NonNull UnaryOperator<I> decorator) {
        this.callStack = decorator.apply(this.callStack);
        LOG.trace("Command {} decorated: {}", cmdName(), this.callStack);
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F onSuccess(@NonNull Consumer<Optional<R>> listener) {
        if (this.successListeners == null) {
            this.successListeners = new ArrayList<>();
        }
        this.successListeners.add(listener);
        return (F) this;
    }

    @SuppressWarnings("unchecked")
    public F onFailure(@NonNull Consumer<RuntimeException> failureListener) {
        if (this.failureListeners == null) {
            this.failureListeners = new ArrayList<>();
        }
        this.failureListeners.add(failureListener);
        return (F) this;
    }

    protected void notifyOnSuccess(R result) {
        if (this.successListeners == null) {
            return;
        }
        var optionalResult = Optional.ofNullable(result);
        for (var listener : this.successListeners) {
            try {
                listener.accept(optionalResult);
            } catch (Exception ex) {
                LOG.error("Command {} failed sending success notification", cmdName(), ex);
            }
        }
    }

    protected void notifyOnFailure(RuntimeException runtimeException) {
        if (this.failureListeners == null) {
            return;
        }
        for (var listener : this.failureListeners) {
            try {
                listener.accept(runtimeException);
            } catch (Exception ex) {
                LOG.error("Command {} failed sending failure notification", cmdName(), ex);
            }
        }
    }

    protected String cmdName() {
        return this.command.getClass().getSimpleName();
    }
}
