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
    private final Map<String, org.springframework.boot.actuate.health.Health> dependencyStatus = new ConcurrentHashMap<>();
    
    public ReadinessProbe(List<DependencyHealthChecker> dependencyCheckers) {
        this.dependencyCheckers = dependencyCheckers;
    }

    @Override
    public org.springframework.boot.actuate.health.Health health() {
        // Verifica todas as dependências
        boolean allHealthy = dependencyCheckers.stream()
            .allMatch(checker -> {
                org.springframework.boot.actuate.health.Health health = checker.checkHealth();
                dependencyStatus.put(checker.getClass().getSimpleName(), health);
                return health.getStatus().equals(org.springframework.boot.actuate.health.Status.UP);
            });
        
        // Constrói detalhes de todas as dependências
        Map<String, Object> details = dependencyStatus.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().getDetails()
            ));
        
        return allHealthy 
            ? Health.up().withDetails(details).build()
            : Health.down().withDetails(details).build();
    }
}

/**
 * Funcionamento Atual:
 * 
 * Verificação de Dependências:
 *     Para cada DependencyHealthChecker, verifica o status
 *     Armazena o resultado no mapa dependencyStatus
 * 
 * Construção do Relatório:
 *     Cria um mapa com os detalhes de todas as dependências
 *     Retorna status UP se todas as dependências estiverem saudáveis
 *     Retorna status DOWN se qualquer dependência estiver com problemas
 * 
 * Endpoint:
 *     Continuará disponível em /actuator/health/readiness
 *     Retornará detalhes completos de todas as dependências verificadas
 */