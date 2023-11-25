package solomon

import solomon.configs.Config
import solomon.core.TestRunnableCmd
import solomon.core.TestRunnableCmdDecorator
import solomon.spi.Handler
import spock.lang.Specification

class ExecutionSpec extends Specification {
    def command = new TestRunnableCmd()
    def cmdHandler = Handler.RUNNABLE as Handler<TestRunnableCmd, TestRunnableCmd>
    def config = Config.emptyConfig()
    def flow = new Execution<>(command, cmdHandler, config)

    def "Fluently configures command"() {
        when:
        def cmdInSetup = null
        def flow2 = flow.setup(cmd -> {
            cmdInSetup = cmd
        })
        then:
        cmdInSetup == command
        flow2 == flow
    }

    def "Fluently configures context"() {
        when:
        def ctxInSetup = null
        def flow2 = flow.setupContext(ctx -> {
            ctxInSetup = ctx
        })
        then:
        ctxInSetup == flow
        flow2 == flow
    }

    def "Fluently decorates command"() {
        given:
        def decorator = new TestRunnableCmdDecorator()
        when:
        flow.decorate(decorator).execute()
        then:
        decorator.counterBefore == 1
        decorator.counterAfter == 1
    }

    def "Fluently decorates before command"() {
        given:
        def counter = 0
        when:
        flow.decorateBefore(ctx -> counter += 1)
                .execute()
        then:
        counter == 1
    }

    def "Fluently decorates after command"() {
        given:
        def counter = 0
        when:
        flow.decorateAfter((ctx, result) -> counter += 1)
                .execute()
        then:
        counter == 1
    }

    def "Executes command and returns value"() {
        when:
        def output = flow.execute()
        then:
        output == command
        output.runCounter == 1
    }

    def "Executes command and converts value"() {
        when:
        def mapper = cmd -> "Value=${cmd.runCounter}"
        def output = flow.execute(mapper)
        then:
        output instanceof GString
    }
}