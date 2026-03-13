package com.localpayment.lp_test_logs.cron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class LogCron {

    private static final Logger log = LoggerFactory.getLogger(LogCron.class);

    @Scheduled(fixedRateString = "5000")
    public void startConsumer() {
        int maxNumber = 100000;
        int randomNumber = ThreadLocalRandom.current().nextInt(maxNumber);

        MDC.put("trace_id", String.valueOf(randomNumber));
        MDC.put("internal_id", UUID.randomUUID().toString());
        log.info("[name_log: test_log] esto es un log de test");
        MDC.remove("trace_id");
        MDC.remove("internal_id");
    }
}
