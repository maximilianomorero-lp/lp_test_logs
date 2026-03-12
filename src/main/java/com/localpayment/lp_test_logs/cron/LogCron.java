package com.localpayment.lp_test_logs.cron;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Component
public class LogCron {
    @Scheduled(fixedRateString = "5000")
    public void startConsumer() {
        Random random = new Random();
        int maxNumber = 100000; // Rango máximo del número aleatorio positivo
        double randomDouble = random.nextDouble();
        Integer randomNumber = (int) (randomDouble * maxNumber);

        MDC.put("trace_id", randomNumber.toString());
        UUID uuid = UUID.randomUUID();
        MDC.put("internal_id", uuid.toString());
        //log.info("[name_log: test_log] esto es un log de test");
        MDC.remove("trace_id");
        MDC.remove("internal_id");
    }
}
