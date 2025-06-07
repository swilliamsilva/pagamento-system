package com.pagamento.common.health;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.actuate.health.HealthEndpointGroups;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import org.springframework.boot.actuate.health.HealthEndpointGroups;

@Configuration
public class HealthCheckConfig {

    @Bean
    public HealthEndpointGroups healthEndpointGroups() {
        return HealthEndpointGroups.of(
            Map.of(
                "readiness", 
                new DefaultHealthEndpointGroup(
                    true, 
                    ShowDetails.ALWAYS,
                    Set.of()
                )
            ),
            new DefaultHealthEndpointGroup(
                true, 
                ShowDetails.ALWAYS,
                Set.of()
            )
        );
    }

    @Bean
    public HealthContributorRegistry healthContributorRegistry(
            List<HealthContributor> contributors) {
        return new AutoConfiguredHealthContributorRegistry(
            contributors.stream()
                .collect(Collectors.toMap(
                    c -> c.getClass().getSimpleName(),
                    java.util.function.Function.identity()
                ))
        );
    }
}