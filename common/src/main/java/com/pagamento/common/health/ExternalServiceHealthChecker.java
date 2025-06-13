package com.pagamento.common.health;

import com.pagamento.common.enums.CustomHttpStatus;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;

@Component("externalServiceHealthIndicator")
public class ExternalServiceHealthChecker implements HealthIndicator {

    private final RestTemplate restTemplate;
    
    @Value("${external.service.health.url}")
    private String healthUrl;
    
    @Value("${external.service.name}")
    private String serviceName;

    public ExternalServiceHealthChecker(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        Instant start = Instant.now();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            Duration responseTime = Duration.between(start, Instant.now());
            return createHealthResponse(response.getStatusCode(), responseTime);
        } catch (Exception ex) {
            Duration responseTime = Duration.between(start, Instant.now());
            return createErrorResponse(ex, responseTime);
        }
    }

    private Health createHealthResponse(HttpStatus status, Duration responseTime) {
        boolean isHealthy = status.is2xxSuccessful();
        Health.Builder builder = isHealthy ? Health.up() : Health.down();

        return builder
            .withDetail("service", serviceName)
            .withDetail("status", CustomHttpStatus.fromCode(status.value()).toString())
            .withDetail("responseTime", responseTime.toMillis() + "ms")
            .build();
    }

    private Health createErrorResponse(Exception ex, Duration responseTime) {
        return Health.down()
            .withDetail("service", serviceName)
            .withDetail("error", ex.getClass().getSimpleName())
            .withDetail("message", ex.getMessage())
            .withDetail("responseTime", responseTime.toMillis() + "ms")
            .build();
    }
}