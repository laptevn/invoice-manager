package com.invoice.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import org.springframework.boot.actuate.health.HealthIndicator;

@Component
public class HealthController implements HealthIndicator {
    private final Logger logger = LoggerFactory.getLogger(HealthController.class);
    private final HealthChecker healthChecker;

    public HealthController(HealthChecker healthChecker) {
        this.healthChecker = healthChecker;
    }

    @Override
    public Health health() {
        Health result = healthChecker.check();
        logger.info("Health status is {}", result);
        return result;
    }
}