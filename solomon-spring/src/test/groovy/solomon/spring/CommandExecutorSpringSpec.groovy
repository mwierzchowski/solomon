package solomon.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solomon.CommandExecutor
import solomon.spring.test.TestApplication
import solomon.spring.test.TestCommandRunnable
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class CommandExecutorSpringSpec extends Specification {
    @Autowired CommandExecutor executor

    def "Executes command"() {
        when:
        executor.runnable(TestCommandRunnable).execute()
        executor.runnable(TestCommandRunnable).execute()
        then:
        noExceptionThrown()
    }
}