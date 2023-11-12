package solomon.flows


import groovy.util.logging.Slf4j
import solomon.decorators.LoggingDecorator
import spock.lang.Specification

@Slf4j
class CommandFlowRunnableSpec extends Specification {
    def command = new RunnableCmd()
    def commandFlow = new CommandFlowRunnable(command, null)

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
        commandFlow.decorate(new LoggingDecorator())
                .execute()
        then:
        noExceptionThrown()
    }

    def "Decorates command inline"() {
        when:
        commandFlow.decorateBefore {cmd -> {
                    log.info("Before")
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
}