package com.localpayment.lp_test_logs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LpTestLogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LpTestLogsApplication.class, args);
	}

}
