package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;

@Component
public class DatabaseHealthChecker implements DependencyHealthChecker {

    private final JdbcTemplate jdbcTemplate;
    
    public DatabaseHealthChecker(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public Health checkHealth() {
        Instant start = Instant.now();
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            Duration duration = Duration.between(start, Instant.now());
            
            // Correção: Usando o Builder corretamente
            Health.Builder builder = Health.up();
            builder.withDetail("response_time_ms", duration.toMillis());
            builder.withDetail("result", result);
            return builder.build();
            
        } catch (Exception e) {
            // Correção: Usando o Builder corretamente
            Health.Builder builder = Health.down();
            builder.withDetail("error", e.getMessage());
            builder.withDetail("response_time_ms", Duration.between(start, Instant.now()).toMillis());
            return builder.build();
        }
    }
}