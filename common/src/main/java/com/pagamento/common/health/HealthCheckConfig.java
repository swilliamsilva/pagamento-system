package com.pagamento.common.health;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthCheckConfig {

    @Bean
    public HealthContributorRegistry healthContributorRegistry(
            List<HealthContributor> contributors) {
        
        Map<String, HealthContributor> contributorMap = contributors.stream()
            .collect(Collectors.toMap(
                c -> c instanceof HealthIndicator ? 
                    ((HealthIndicator) c).getClass().getSimpleName() : 
                    c.getClass().getSimpleName(),
                c -> c
            ));
        
        return new HealthContributorRegistry() {
            @Override
            public HealthContributor getContributor(String name) {
                return contributorMap.get(name);
            }

            @Override
            public Stream<NamedContributor<HealthContributor>> stream() {
                return contributorMap.entrySet().stream()
                    .map(entry -> NamedContributor.of(entry.getKey(), entry.getValue()));
            }

			@Override
			public void registerContributor(String name, HealthContributor contributor) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public HealthContributor unregisterContributor(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterator<NamedContributor<HealthContributor>> iterator() {
				// TODO Auto-generated method stub
				return null;
			}
        };
    }
}