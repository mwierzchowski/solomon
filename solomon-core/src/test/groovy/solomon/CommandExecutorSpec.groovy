package solomon

import solomon.helpers.TestRunnableCmd
import solomon.helpers.TestSupplierCmd
import spock.lang.Specification

class CommandExecutorSpec extends Specification {
    def "Provides builder"() {
        when:
        def builder = CommandExecutor.builder()
        then:
        builder != null
        builder.class == CommandExecutorBuilder
    }

    def "Creates runnable command execution"() {
        given:
        def cmdClass = TestRunnableCmd
        def executor = CommandExecutor.builder().build()
        when:
        def execution = executor.runnable(cmdClass)
        then:
        execution.command.class == cmdClass
    }

    def "Creates runnable execution and configures command"() {
        given:
        def cmdClass = TestRunnableCmd
        def executor = CommandExecutor.builder().build()
        when:
        def executionCommand = null
        def execution = executor.runnable(cmdClass, configured -> {
            executionCommand = configured
        })
        then:
        executionCommand == execution.command
    }

    def "Creates supplier command execution"() {
        given:
        def cmdClass = TestSupplierCmd
        def executor = CommandExecutor.builder().build()
        when:
        def execution = executor.supplier(cmdClass)
        then:
        execution.command.class == cmdClass
    }

    def "Creates supplier execution and configures command"() {
        given:
        def cmdClass = TestSupplierCmd
        def executor = CommandExecutor.builder().build()
        when:
        def executionCommand = null
        def execution = executor.supplier(cmdClass, configured -> {
            executionCommand = configured
        })
        then:
        executionCommand == execution.command
    }
}