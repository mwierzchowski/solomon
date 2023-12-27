package solomon

import solomon.helpers.TestObserver
import solomon.helpers.TestRunnableCmd
import solomon.helpers.TestRunnableCmdDecorator
import solomon.helpers.TestSupplierCmd
import solomon.services.DefaultFactory
import spock.lang.Specification

import static solomon.Utils.cast

class ExecutionSpec extends Specification {
    def factory = new DefaultFactory()
    def runnableCmd = new TestRunnableCmd()
    def supplierCmd = new TestSupplierCmd(123)
    def globalConfig = new Config()
    def runnableExecution = new Execution<>(factory, runnableCmd, cast(Handler.RUNNABLE), globalConfig)
    def supplierExecution = new Execution<>(factory, supplierCmd, cast(Handler.SUPPLIER), globalConfig)

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

    def "Observers success"() {
        given:
        def inlineSuccessObserverCounter = 0
        def observer = new TestObserver()
        when:
        runnableExecution.observe(observer)
                .observeSuccess((a, b) -> inlineSuccessObserverCounter += 1)
                .execute()
        then:
        observer.successCounter == 1
        observer.failureCounter == 0
        inlineSuccessObserverCounter == 1
    }

    def "Observers failure"() {
        given:
        def inlineFailureObserverCounter = 0
        def listener = new TestObserver()
        when:
        runnableExecution.observe(listener)
                .observeFailure((a, b) -> inlineFailureObserverCounter += 1)
                .decorateAfter((a, b) -> { throw new IllegalArgumentException()})
                .execute().get()
        then:
        thrown IllegalArgumentException
        listener.successCounter == 0
        listener.failureCounter == 1
        inlineFailureObserverCounter == 1
    }

    def "Executes runnable command and returns value"() {
        when:
        def output = runnableExecution.execute().get()
        then:
        output == runnableCmd
        output.runCounter == 1
    }

    def "Executes supplier command and returns value"() {
        when:
        def output = supplierExecution.execute().get()
        then:
        output == 123
    }

    def "Executes command and converts value"() {
        when:
        def mapper = cmd -> "Value=${cmd.runCounter}"
        def output = runnableExecution.execute().getMapped(mapper)
        then:
        output instanceof GString
    }

    def "Uses global addons during execution"() {
        given:
        def decorator = new TestRunnableCmdDecorator()
        globalConfig.add(decorator)
        runnableExecution = new Execution<>(factory, runnableCmd, cast(Handler.RUNNABLE), globalConfig)
        when:
        runnableExecution.execute()
        then:
        decorator.counterBefore == 1
        decorator.counterAfter == 1
    }
}