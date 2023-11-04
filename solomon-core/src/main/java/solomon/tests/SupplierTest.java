package solomon.tests;

import solomon.CommandExecutorJdk;

public class SupplierTest {
    public static void main(String[] args) {
        var result = new CommandExecutorJdk().supplier(SupplierCmd.class, cmd -> {
                    cmd.x = 42;
                })
                .decorate(SupplierDecorator::new)
                .onSuccess(r -> System.out.println("Dostalem wynik " + r.get()))
                .onFailure(ex -> System.out.println("Dostalem wyjatek " + ex))
                .onFailure(ex -> System.out.println("Dostalem wyjatek2 " + ex))
                .defaultResult(123)
                .execute(String::valueOf);
        System.out.println("Dostalem " + result + " [" + result.getClass().getName() + "]");
    }
}
