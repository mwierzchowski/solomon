package solomon.helpers

import solomon.addons.Observer

class TestObserver implements Observer<Object, Object> {
    def successCounter = 0
    def failureCounter = 0

    @Override
    void onSuccess(Object command, Object value) {
        successCounter += 1
    }

    @Override
    void onFailure(Object command, RuntimeException exception) {
        failureCounter += 1
    }
}
