package com.localpayment.lp_test_logs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(excludeAutoConfiguration = SystemMetricsAutoConfiguration.class)
class LpTestLogsApplicationTests {

	@Test
	void contextLoads() {
	}

}
