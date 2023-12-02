package solomon.helpers

import solomon.Config
import solomon.Execution

class TestContext extends Execution<Object, Object> {
    TestContext() {
        super(new DummyCommand(), new DummyHandler(), new Config())
    }
}
