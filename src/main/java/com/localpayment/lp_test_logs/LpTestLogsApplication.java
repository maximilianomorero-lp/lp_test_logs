package com.localpayment.lp_test_logs;

import com.localpayment.lp_test_logs.config.CustomExceptionHandler;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.HandlerExceptionResolver;

@SpringBootApplication
@EnableScheduling
public class LpTestLogsApplication {

	public static void main(String[] args) {

		SpringApplication.run(LpTestLogsApplication.class, args);
	}

	@Bean
	public HandlerExceptionResolver customExceptionHandler() {
		return new CustomExceptionHandler();
	}

}
