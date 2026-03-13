package com.localpayment.lp_test_logs.cron;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LogCronTest {

    private final LogCron logCron = new LogCron();

    @Test
    void testStartConsumer() {
        assertDoesNotThrow(() -> logCron.startConsumer());
    }
}
