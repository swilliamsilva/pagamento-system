package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * ReadinessProbe indica se o aplicativo está pronto para receber tráfego.
 * Verifica todas as dependências críticas antes de reportar "UP".
 * 
 * @apiNote Health Check para Kubernetes
 * @endpoint /actuator/health/readiness
 */
@Component("readinessProbe")
public class ReadinessProbe implements HealthIndicator {

    private final List<DependencyHealthChecker> dependencyCheckers;
    private final Map<String, Health> dependencyStatus = new ConcurrentHashMap<>();
    
    public ReadinessProbe(List<DependencyHealthChecker> dependencyCheckers) {
        this.dependencyCheckers = dependencyCheckers;
    }

    @Override
    public Health health() {
        // Verifica todas as dependências
        boolean allHealthy = dependencyCheckers.stream()
            .allMatch(checker -> {
                Health health = checker.checkHealth();
                dependencyStatus.put(checker.getClass().getSimpleName(), health);
                return health.getStatus().equals(Health.status("UP").build().getStatus());
        /*The method build() is undefined for the type Object*/
            });
        
        // Constrói detalhes de todas as dependências
        Map<Object, Object> details = dependencyStatus.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().getDetails()
            ));
        
        return allHealthy 
            ? Health.up().withDetails(details).build()
            		/*The method withDetails(Map<Object,Object>) is undefined for the type Health.Builder*/
            : Health.down().withDetails(details).build();
            /*The method withDetails(Map<Object,Object>) is undefined for the type Health.Builder*/
    }
}
