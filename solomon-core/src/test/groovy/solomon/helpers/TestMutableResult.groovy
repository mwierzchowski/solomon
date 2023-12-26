package solomon.helpers

import solomon.Config
import solomon.Execution
import solomon.services.DefaultFactory

class TestMutableResult extends Execution<Object, Object> {
    TestMutableResult() {
        super(new DefaultFactory(), new DummyCommand(), new DummyHandler(), new Config())
    }
}
