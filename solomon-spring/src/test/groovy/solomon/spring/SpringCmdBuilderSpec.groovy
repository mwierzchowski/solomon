package solomon.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import solomon.CmdBuilder
import solomon.spring.test.TestApplication
import solomon.spring.test.TestCommandRunnable
import spock.lang.Specification

@SpringBootTest(classes = TestApplication)
class SpringCmdBuilderSpec extends Specification {
    @Autowired CmdBuilder executor

    def "Executes command"() {
        when:
        executor.createRunnable(TestCommandRunnable).execute()
        executor.createRunnable(TestCommandRunnable).execute()
        then:
        noExceptionThrown()
    }
}