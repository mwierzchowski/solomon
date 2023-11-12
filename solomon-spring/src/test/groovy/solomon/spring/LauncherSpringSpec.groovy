package solomon.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solomon.Launcher
import solomon.spring.test.TestApplication
import solomon.spring.test.TestCommandRunnable
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class LauncherSpringSpec extends Specification {
    @Autowired Launcher executor

    def "Executes command"() {
        when:
        executor.createRunnable(TestCommandRunnable).execute()
        executor.createRunnable(TestCommandRunnable).execute()
        then:
        noExceptionThrown()
    }
}