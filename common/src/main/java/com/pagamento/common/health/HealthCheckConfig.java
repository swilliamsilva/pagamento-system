package com.pagamento.common.health;

import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.health.HealthProperties.Show;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.HealthEndpointGroup;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.actuate.health.CompositeHealthContributor;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Function;

import io.vavr.collection.Map;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuração adicional para health checks.
 * 
 * @apiNote Configura endpoints de health check
 */

public class HealthCheckConfig {

    @Bean
    public HealthEndpointGroups healthEndpointGroups() {
        return HealthEndpointGroups.of(
            Map.of(
                "readiness", HealthEndpointGroup.showDetails(Show.ALWAYS),
                /* The method showDetails(SecurityContext) in the type HealthEndpointGroup is not applicable for the arguments (HealthProperties.Show)*/
                "liveness", HealthEndpointGroup.showDetails(Show.ALWAYS)
                /*The method showDetails(SecurityContext) in the type HealthEndpointGroup is not applicable for the arguments (HealthProperties.Show) */
            ),
            HealthEndpointGroup.showDetails(Show.ALWAYS)
            /*The method showDetails(SecurityContext) in the type HealthEndpointGroup is not applicable for the arguments (HealthProperties.Show) */
        );
    }

    @Bean
    public HealthContributorRegistry healthContributorRegistry(
            List<HealthContributor> contributors) {
        CompositeHealthContributor composite = CompositeHealthContributor.fromMap(
            contributors.stream()
                .collect(Collectors.toMap(
                    c -> c.getClass().getSimpleName(),
                    Function.identity()
                    
                    /*The method identity() is undefined for the type Function */
                ))
        );
        return new AutoConfiguredHealthContributorRegistry();
    }
}

