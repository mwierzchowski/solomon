package solomon.helpers

import solomon.Config
import solomon.Execution
import solomon.services.DefaultFactory

class TestResult extends Execution<Object, Object> {
    TestResult() {
        super(new DefaultFactory(), new DummyCommand(), new DummyHandler(), new Config())
    }
}
