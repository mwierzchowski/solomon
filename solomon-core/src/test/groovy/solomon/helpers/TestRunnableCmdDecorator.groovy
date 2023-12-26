package solomon.helpers

import solomon.ExecutionContext
import solomon.Result
import solomon.addons.Decorator

class TestRunnableCmdDecorator implements Decorator<TestRunnableCmd, Object> {
    public int counterBefore = 0
    public int counterAfter = 0

    @Override
    void before(ExecutionContext<TestRunnableCmd> context) {
        this.counterBefore += 1
    }

    @Override
    void after(ExecutionContext<TestRunnableCmd> context, Result<Object> result) {
        this.counterAfter += 1
    }
}
