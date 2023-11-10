package solomon.spring.test

import groovy.util.logging.Slf4j
import solomon.spring.annotation.Command

@Slf4j
@Command
class TestCommandRunnable implements Runnable {
    TestCommandRunnable() {
        log.info("Creating")
    }

    @Override
    void run() {
        log.info("Running")
    }
}