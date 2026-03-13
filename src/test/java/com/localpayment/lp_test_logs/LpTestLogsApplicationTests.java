package com.localpayment.lp_test_logs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableAutoConfiguration(exclude = SystemMetricsAutoConfiguration.class)
class LpTestLogsApplicationTests {

	@Test
	void contextLoads() {
	}

}
