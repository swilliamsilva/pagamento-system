package com.pagamento.common.health;

import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

/**
 * Configuração adicional para health checks.
 * 
 * @apiNote Configura endpoints de health check
 */
@Configuration
public class HealthCheckConfig {

    /**
     * Configura endpoint customizado para readiness
     */
    @Bean
    public HealthEndpointProperties healthEndpointProperties() {
        HealthEndpointProperties properties = new HealthEndpointProperties();
        properties.getGroup().put("readiness", List.of("readinessProbe"));
        properties.getGroup().put("liveness", List.of("livenessProbe"));
        return properties;
    }

    /**
     * Registra health indicators customizados.
     */
    @Bean
    public HealthContributorRegistry healthContributorRegistry(
        List<HealthContributor> contributors,
        HealthEndpoint healthEndpoint
    ) {
        contributors.forEach(healthEndpoint::registerContributor);
        return healthEndpoint.getContributorRegistry();
    }
}
