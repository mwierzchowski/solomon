package solomon.runnable

import groovy.util.logging.Slf4j;
import solomon.CommandExecutorJdk;
import spock.lang.Specification;

class RunnableVoidCmdSpec extends Specification {
    def executor = new CommandExecutorJdk()

    def "Executes command"() {
        when:
        executor.runnable(RunnableCmd)
                .initialize {
                    it.x = 42
                }
                .execute()
        then:
        noExceptionThrown()
    }

    def "Test that fails"() {
        when:
        def x = 1 + 1
        then:
        x == 3
    }

//    public static void main(String[] args) {
//        new CommandExecutorJdk().runnable(RunnableVoidCmd.class)
//                .initialize(cmd -> {
//                    cmd.name = "Marcin";
//                    cmd.age = 42;
//                })
//                .decorateInline(cmd -> {
//                    System.out.println(">>>>>>>> BEFORE");
//                    cmd.run();
//                    System.out.println("<<<<<<<<<<<< AFTER");
//                })
//                .onSuccess(r -> System.out.println("XX"))
//                .decorate(RunnableDecorator::new)
//                .execute();
//    }

    @Slf4j
    static class RunnableCmd implements Runnable {
        public int x;

        @Override
        void run() {
          log.info("parameter x = {}", x)

        }
    }
}