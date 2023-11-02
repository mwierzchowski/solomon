package solomon.tests;

import solomon.CommandExecutorJdk;

public class RunnableTest {
    public static void main(String[] args) {
        new CommandExecutorJdk().runnable(RunnableCmd.class)
                .initialize(cmd -> {
                    cmd.name = "Marcin";
                    cmd.age = 42;
                })
                .decorateInline(cmd -> {
                    System.out.println(">>>>>>>> BEFORE");
                    cmd.run();
                    System.out.println("<<<<<<<<<<<< AFTER");
                })
                .onSuccess(r -> System.out.println("XX"))
                .decorate(RunnableDecorator::new)
                .execute();
    }
}