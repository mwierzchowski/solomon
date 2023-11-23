package solomon2.core

import solomon2.spi.CmdHandler
import spock.lang.Specification

class FlowSpec extends Specification {
    def command = new TestRunnableCmd()
    def cmdHandler = CmdHandler.RUNNABLE as CmdHandler<TestRunnableCmd, TestRunnableCmd>
    def configSet = new ConfigSet(null);
    def flow = new Flow<>(command, cmdHandler, configSet)

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