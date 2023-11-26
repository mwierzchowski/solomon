package solomon

import solomon.helpers.TestListener
import solomon.helpers.TestRunnableCmd
import solomon.helpers.TestRunnableCmdDecorator
import solomon.helpers.TestSupplierCmd
import spock.lang.Specification

import static solomon.Utils.cast

class ExecutionSpec extends Specification {
    def runnableCmd = new TestRunnableCmd()
    def supplierCmd = new TestSupplierCmd(123)
    def globalConfig = new Config()
    def runnableExecution = new Execution<>().tap {
        it.command = runnableCmd
        it.handler = cast(Handler.RUNNABLE)
        it.config = globalConfig
    }
    def supplierExecution = new Execution<>().tap {
        it.command = supplierCmd
        it.handler = cast(Handler.SUPPLIER)
        it.config = globalConfig
    }

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

    def "Listens to the success"() {
        given:
        def inlineSuccessListenerCounter = 0
        def listener = new TestListener()
        when:
        runnableExecution.listen(listener)
                .listenOnSuccess((a, b) -> inlineSuccessListenerCounter += 1)
                .execute()
        then:
        listener.successCounter == 1
        listener.failureCounter == 0
        inlineSuccessListenerCounter == 1
    }

    def "Listens to the failure"() {
        given:
        def inlineFailureListenerCounter = 0
        def listener = new TestListener()
        when:
        runnableExecution.listen(listener)
                .listenOnFailure((a, b) -> inlineFailureListenerCounter += 1)
                .decorateAfter((a, b) -> { throw new IllegalArgumentException()})
                .execute()
        then:
        thrown IllegalArgumentException
        listener.successCounter == 0
        listener.failureCounter == 1
        inlineFailureListenerCounter == 1
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
        globalConfig.add(decorator)
        runnableExecution = new Execution<>().tap {
            it.command = runnableCmd
            it.handler = cast(Handler.RUNNABLE)
            it.config = globalConfig
        }
        when:
        runnableExecution.execute()
        then:
        decorator.counterBefore == 1
        decorator.counterAfter == 1
    }
}