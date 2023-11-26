package solomon.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solomon.CommandExecutor
import solomon.spring.helpers.TestApplication
import solomon.spring.helpers.TestCommandRunnable
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class AutoConfigurationSpec extends Specification {
    @Autowired CommandExecutor executor

    def "Injects executor"() {
        when:
        executor.runnable(TestCommandRunnable).execute()
        then:
        noExceptionThrown()
    }
}