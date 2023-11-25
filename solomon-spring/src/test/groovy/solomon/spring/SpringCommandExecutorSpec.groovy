package solomon.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solomon.CommandExecutor
import solomon.spring.helpers.TestApplication
import solomon.spring.helpers.TestCommandRunnable
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class SpringCommandExecutorSpec extends Specification {
    @Autowired CommandExecutor executor

    def "Executes command"() {
        when:
        executor.runnable(TestCommandRunnable).execute()
        executor.runnable(TestCommandRunnable).execute()
        then:
        noExceptionThrown()
    }
}