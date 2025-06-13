/* ========================================================
# Classe: HealthCheckJdbcTemplate
# Módulo: Common Health Check - Database Verification
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7 e Maven - Junho de 2025
# ======================================================== */
package com.pagamento.common.health;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Repository
@Schema(description = "Classe wrapper para execução de consultas SQL no contexto de health check")
public class HealthCheckJdbcTemplate {

    private final JdbcTemplate springJdbcTemplate;

    public HealthCheckJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.springJdbcTemplate = Objects.requireNonNull(jdbcTemplate);
    }

    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return springJdbcTemplate.queryForObject(sql, requiredType);
    }

    public Integer healthCheck() {
        return queryForObject("SELECT 1", Integer.class);
    }
}