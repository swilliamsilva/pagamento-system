package com.pagamento.common.health;


import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.LinkedHashMap;
import java.util.Map;

public class CustomHealthEndpoint {

    private final Map<String, HealthIndicator> indicators;

    public CustomHealthEndpoint(Map<String, HealthIndicator> indicators) {
        this.indicators = indicators;
    }

    public Health health() {
        Map<String, Object> details = new LinkedHashMap<>();
        boolean hasDown = false;

        for (Map.Entry<String, HealthIndicator> entry : indicators.entrySet()) {
            Health health = entry.getValue().health();
            details.put(entry.getKey(), health);
            if (health.getStatus().getCode().equalsIgnoreCase("DOWN")) {
                hasDown = true;
            }
        }

        return hasDown ? Health.down().withDetails(details).build()
                       : Health.up().withDetails(details).build();
    }
}
