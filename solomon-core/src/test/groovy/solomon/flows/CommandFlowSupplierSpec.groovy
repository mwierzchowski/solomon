package solomon.flows

import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import spock.lang.Specification

import java.util.function.Supplier

@Slf4j
class CommandFlowSupplierSpec extends Specification {
    def cmd = new SupplierCmd()
    def cmdFlow = new CommandFlowSupplier(cmd)

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
        def result = cmdFlow.decorate(SupplierDecorator::new)
                .execute()
        then:
        result == 1000
    }

    def "Decorates command inline"() {
        when:
        cmdFlow.decorateInline { cmd -> {
                    log.info("Before")
                    def result = cmd.get()
                    log.info("After")
                    return result
                }}
                .execute()
        then:
        noExceptionThrown()
    }

    def "Listens for command success"() {
        when:
        cmdFlow.onSuccess {
                    log.info("Success " + it.get())
                }
                .execute()
        then:
        noExceptionThrown()
    }

    def "Listens for command failure"() {
        given:
        def exception = new RuntimeException("test")
        when:
        cmdFlow.decorateInline {
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
        def result = cmdFlow.decorateInline {
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
    static class SupplierDecorator<C> implements Supplier<C> {
        private final Supplier<C> cmd

        @Override
        C get() {
            return cmd.get() * 10
        }
    }
}