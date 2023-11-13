package solomon.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solomon.CmdFactory
import solomon.spring.test.TestApplication
import solomon.spring.test.TestCommandRunnable
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class SpringCmdFactorySpec extends Specification {
    @Autowired CmdFactory executor

    def "Executes command"() {
        when:
        executor.createRunnable(TestCommandRunnable).execute()
        executor.createRunnable(TestCommandRunnable).execute()
        then:
        noExceptionThrown()
    }
}