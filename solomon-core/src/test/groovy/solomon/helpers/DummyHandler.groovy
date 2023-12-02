package solomon.helpers

import solomon.Handler
import solomon.Result

class DummyHandler implements Handler<Object, Object> {
    void accept(Object o, Result<Object> objectResult) {}
}
