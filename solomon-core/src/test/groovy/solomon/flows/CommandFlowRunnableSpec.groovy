package solomon.flows

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import spock.lang.Specification

@Slf4j
class CommandFlowRunnableSpec extends Specification {
    def command = new RunnableCmd()
    def commandFlow = new CommandFlowRunnable(command)

    def "Executes command"() {
        when:
        commandFlow.execute()
        then:
        noExceptionThrown()
    }

    def "Initializes command"() {
        when:
        commandFlow.initialize {
                    it.x = 42
                }
                .execute()
        then:
        noExceptionThrown()
    }

    def "Decorates command with decorator"() {
        when:
        commandFlow.decorate(RunnableDecorator::new)
                .execute()
        then:
        noExceptionThrown()
    }

    def "Decorates command inline"() {
        when:
        commandFlow.decorateInline {cmd -> {
                    log.info("Before")
                    cmd.run()
                    log.info("After")
                }}
                .execute()
        then:
        noExceptionThrown()
    }

    def "Listens for command success"() {
        when:
        commandFlow.onSuccess {
                    log.info("Success")
                }
                .execute()
        then:
        noExceptionThrown()
    }

    def "Listens for command failure"() {
        given:
        def exception = new RuntimeException("test")
        when:
        commandFlow.decorateInline {
                    throw exception
                }
                .onFailure {
                    log.info("Error", it)
                }
                .execute()
        then:
        thrown RuntimeException
    }

    @Slf4j
    static class RunnableCmd implements Runnable {
        public int x

        @Override
        void run() {
          log.info("parameter x = {}", x)

        }
    }

    @Slf4j
    @TupleConstructor(includeFields = true)
    static class RunnableDecorator implements Runnable {
        private final Runnable cmd

        @Override
        void run() {
            log.info("Before")
            cmd.run()
            log.info("After")
        }
    }
}