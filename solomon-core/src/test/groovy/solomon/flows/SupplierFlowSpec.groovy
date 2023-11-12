package solomon.flows

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import solomon.Decorator
import spock.lang.Specification

import java.util.function.Supplier

@Slf4j
class SupplierFlowSpec extends Specification {
    def cmd = new SupplierCmd()
    def cmdFlow = new SupplierFlow(cmd, null)

    def "Executes command"() {
        when:
        cmdFlow.execute()
        then:
        noExceptionThrown()
    }

    def "Initializes command"() {
        when:
        def result = cmdFlow.initialize(cmd -> {
                        cmd.x = 42
                    })
                    .execute()
        then:
        result == 4200
    }

    def "Decorates command with decorator"() {
        when:
        def result = cmdFlow.
                initialize ({ cmd ->
                    cmd.x = 10
                })
                .decorate(new SupplierCmdDecorator(20))
                .execute()
        then:
        result == 2000
    }

    def "Decorates command inline"() {
        when:
        cmdFlow.decorateBefore { cmd -> {
                    log.info("Before")
                }}
                .execute()
        then:
        noExceptionThrown()
    }

    def "Listens for command success"() {
        when:
        cmdFlow.onSuccess(cmd -> {
                    log.info("Success " + cmd)
                })
                .execute()
        then:
        noExceptionThrown()
    }


    def "Listens for command failure"() {
        given:
        def exception = new RuntimeException("test")
        when:
        cmdFlow.decorateBefore {
                    throw exception
                }
                .onFailure {
                    log.info("Error", it)
                }
                .execute()
        then:
        thrown RuntimeException
    }

    def "Uses default value"() {
        given:
        def defaultValue = 123
        def exception = new RuntimeException("test")
        when:
        def result = cmdFlow.decorateBefore {
                    throw exception
                }
                .defaultResult(defaultValue)
                .execute()
        then:
        result == defaultValue
    }

    static class SupplierCmd implements Supplier<Integer> {
        public int x = 1

        @Override
        Integer get() {
            return x * 100
        }
    }

    @TupleConstructor(includeFields = true)
    static class SupplierCmdDecorator implements Decorator {
        private final overrideValue;

        @Override
        void before(Object command) {
            command.asType(SupplierCmd).x = this.overrideValue
        }
    }
}