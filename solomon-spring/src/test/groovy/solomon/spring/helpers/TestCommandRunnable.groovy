package solomon.spring.helpers

import groovy.util.logging.Slf4j
import solomon.spring.annotation.CommandBean

@Slf4j
@CommandBean
class TestCommandRunnable implements Runnable {
    TestCommandRunnable() {
        log.info("Creating")
    }

    @Override
    void run() {
        log.info("Running")
    }
}