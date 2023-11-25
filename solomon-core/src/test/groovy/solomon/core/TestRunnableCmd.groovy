package solomon.core

class TestRunnableCmd implements Runnable {
    int runCounter = 0

    @Override
    void run() {
        runCounter += 1
    }
}
