package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;

/**
 * Verifica a saúde de um serviço externo.
 * 
 * @apiNote Health Check para serviços externos (ex: API de bancos)
 * @param serviceName Nome do serviço para identificação
 * @param healthUrl URL do endpoint de health check do serviço
 */
@Component
public class ExternalServiceHealthChecker implements DependencyHealthChecker {

    private final RestTemplate restTemplate;
    private final String serviceName;
    private final String healthUrl;

    public ExternalServiceHealthChecker(
        RestTemplate restTemplate,
        String serviceName,
        String healthUrl
    ) {
        this.restTemplate = restTemplate;
        this.serviceName = serviceName;
        this.healthUrl = healthUrl;
    }
    
    @Override
    public Health checkHealth() {
        Instant start = Instant.now();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            Duration duration = Duration.between(start, Instant.now());
            
            Health.Builder builder = Health.up()
                .withDetail("response_time_ms", duration.toMillis())
                .withDetail("status_code", response.getStatusCodeValue());
            
            if (!response.getStatusCode().is2xxSuccessful()) {
                builder.down()
                    .withDetail("error", "Non-2xx response: " + response.getStatusCode());
            }
            
            return builder.build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .withDetail("response_time_ms", Duration.between(start, Instant.now()).toMillis())
                .build();
        }
    }
}
