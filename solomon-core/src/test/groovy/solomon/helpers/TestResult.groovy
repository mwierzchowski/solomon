package solomon.helpers

import solomon.Config
import solomon.Execution

class TestResult extends Execution<Object, Object> {
    TestResult() {
        super(new DummyCommand(), new DummyHandler(), new Config())
    }
}
