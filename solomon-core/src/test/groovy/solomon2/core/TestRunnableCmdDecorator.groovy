package solomon2.core

import solomon2.ExecutionResult
import solomon2.spi.Context
import solomon2.spi.Decorator

class TestRunnableCmdDecorator implements Decorator<TestRunnableCmd, Object> {
    public int counterBefore = 0
    public int counterAfter = 0

    @Override
    void before(Context<TestRunnableCmd> context) {
        this.counterBefore += 1
    }

    @Override
    void after(Context<TestRunnableCmd> context, ExecutionResult<Object> result) {
        this.counterAfter += 1
    }
}
