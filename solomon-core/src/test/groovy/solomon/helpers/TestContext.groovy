package solomon.helpers

import solomon.Config
import solomon.Execution
import solomon.services.DefaultFactory

class TestContext extends Execution<Object, Object> {
    TestContext() {
        super(new DefaultFactory(), new DummyCommand(), new DummyHandler(), new Config())
    }
}
