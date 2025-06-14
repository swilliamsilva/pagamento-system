package com.pagamento.common.health;

import java.util.List;

import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthCheckConfig {

    @Bean
    public HealthContributorRegistry healthContributorRegistry(List<HealthContributor> contributors) {
        HealthContributorRegistry registry = new HealthContributorRegistry();
        for (HealthContributor contributor : contributors) {
            String name = contributor.getClass().getSimpleName();
            registry.registerContributor(name, contributor);
        }
        return registry;
    }
}
