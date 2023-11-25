package solomon

import solomon.configs.Config
import solomon.helpers.TestRunnableCmd
import solomon.helpers.TestRunnableCmdDecorator
import solomon.helpers.TestSupplierCmd
import solomon.spi.Handler
import spock.lang.Specification

import static solomon.Utils.cast

class ExecutionSpec extends Specification {
    def runnableCmd = new TestRunnableCmd()
    def supplierCmd = new TestSupplierCmd(123)
    def config = Config.emptyConfig()
    def runnableExecution = new Execution<>(runnableCmd, cast(Handler.RUNNABLE), config)
    def supplierExecution = new Execution<>(supplierCmd, cast(Handler.SUPPLIER), config)

    def "Fluently configures command"() {
        when:
        def cmdInSetup = null
        def flow2 = runnableExecution.setup(cmd -> {
            cmdInSetup = cmd
        })
        then:
        cmdInSetup == runnableCmd
        flow2 == runnableExecution
    }

    def "Fluently configures context"() {
        when:
        def ctxInSetup = null
        def flow2 = runnableExecution.setupContext(ctx -> {
            ctxInSetup = ctx
        })
        then:
        ctxInSetup == runnableExecution
        flow2 == runnableExecution
    }

    def "Fluently decorates command"() {
        given:
        def decorator = new TestRunnableCmdDecorator()
        when:
        runnableExecution.decorate(decorator).execute()
        then:
        decorator.counterBefore == 1
        decorator.counterAfter == 1
    }

    def "Fluently decorates before command"() {
        given:
        def counter = 0
        when:
        runnableExecution.decorateBefore(ctx -> counter += 1)
                .execute()
        then:
        counter == 1
    }

    def "Fluently decorates after command"() {
        given:
        def counter = 0
        when:
        runnableExecution.decorateAfter((ctx, result) -> counter += 1)
                .execute()
        then:
        counter == 1
    }

    def "Executes runnable command and returns value"() {
        when:
        def output = runnableExecution.execute()
        then:
        output == runnableCmd
        output.runCounter == 1
    }

    def "Executes supplier command and returns value"() {
        when:
        def output = supplierExecution.execute()
        then:
        output == 123
    }

    def "Executes command and converts value"() {
        when:
        def mapper = cmd -> "Value=${cmd.runCounter}"
        def output = runnableExecution.execute(mapper)
        then:
        output instanceof GString
    }

    def "Uses global addons during execution"() {
        given:
        def decorator = new TestRunnableCmdDecorator()
        config = config.add(decorator)
        runnableExecution = new Execution<>(runnableCmd, cast(Handler.RUNNABLE), config)
        when:
        runnableExecution.execute()
        then:
        decorator.counterBefore == 1
        decorator.counterAfter == 1
    }
}