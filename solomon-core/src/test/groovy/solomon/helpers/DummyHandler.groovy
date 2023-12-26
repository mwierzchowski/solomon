package solomon.helpers

import solomon.Handler
import solomon.MutableResult

class DummyHandler implements Handler<Object, Object> {
    void accept(Object o, MutableResult<Object> objectResult) {}
}
