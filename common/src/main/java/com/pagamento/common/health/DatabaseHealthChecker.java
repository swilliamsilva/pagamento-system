package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;

/**
 * Verifica a saúde da conexão com o banco de dados.
 * 
 * @apiNote Health Check para conexão de banco de dados
 */
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
            
            return Health.up()
                .withDetail("response_time_ms", duration.toMillis())
                .withDetail("result", result)
                .build();
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .withDetail("response_time_ms", Duration.between(start, Instant.now()).toMillis())
                .build();
        }
    }
}
