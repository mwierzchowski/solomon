package solomon.core

import solomon.Context
import solomon.Result
import solomon.spi.Decorator

class TestRunnableCmdDecorator implements Decorator<TestRunnableCmd, Object> {
    public int counterBefore = 0
    public int counterAfter = 0

    @Override
    void before(Context<TestRunnableCmd> context) {
        this.counterBefore += 1
    }

    @Override
    void after(Context<TestRunnableCmd> context, Result<Object> result) {
        this.counterAfter += 1
    }
}
