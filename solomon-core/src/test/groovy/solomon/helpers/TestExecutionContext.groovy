package solomon.helpers

import solomon.Config
import solomon.Execution
import solomon.services.DefaultFactory

class TestExecutionContext extends Execution<Object, Object> {
    TestExecutionContext() {
        super(new DefaultFactory(), new DummyCommand(), new DummyHandler(), new Config())
    }
}
