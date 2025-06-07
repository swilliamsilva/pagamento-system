package com.pagamento.common.health;

import com.pagamento.common.enums.CustomHttpStatus;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;

/**
 * ========================================================
 * HEALTH CHECK DE SERVIÇOS EXTERNOS
 *
 * Monitora a saúde de APIs externas com métricas detalhadas
 *
 * @tag Health Check
 * @operationId externalServiceHealth
 * @summary Monitoramento de APIs externas
 * @description Verifica disponibilidade e performance de serviços integrados
 */
@Component("externalServiceHealthIndicator")
public class ExternalServiceHealthChecker implements HealthIndicator {

    private final RestTemplate restTemplate;

    @Value("${external.service.health.url}")
    private String healthUrl;

    @Value("${external.service.name}")
    private String serviceName;

    public ExternalServiceHealthChecker(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Health health() {
        Instant start = Instant.now();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
            Duration responseTime = Duration.between(start, Instant.now());
            return createHealthResponse(response.getStatusCode(), responseTime);
        } catch (Exception ex) {
            return createErrorResponse(ex, Duration.between(start, Instant.now()));
        }
    }

    private Health createHealthResponse(HttpStatus status, Duration responseTime) {
        boolean isHealthy = status.is2xxSuccessful();
        Health.Builder builder = isHealthy ? Health.up() : Health.down();

        return builder
            .withDetail("service", serviceName)
            .withDetail("status", CustomHttpStatus.fromCode(status.value()).toString())
            .withDetail("responseTime", responseTime.toMillis() + "ms")
            .build();
    }

    private Health createErrorResponse(Exception ex, Duration responseTime) {
        return Health.down()
            .withDetail("service", serviceName)
            .withDetail("error", ex.getClass().getSimpleName())
            .withDetail("message", ex.getMessage())
            .withDetail("responseTime", responseTime.toMillis() + "ms")
            .build();
    }
}


/**
 * ========================================================
 * FLUXO DE OPERAÇÃO - HEALTH CHECK DE SERVIÇOS EXTERNOS
 * ========================================================
 * 
 * 1. ENTRADA:
 *    - Chamada automática pelo Spring Boot Actuator (/actuator/health)
 *    - Configurações via application.properties:
 *      * external.service.health.url: URL do endpoint de health check
 *      * external.service.name: Nome amigável do serviço
 * 
 * 2. PROCESSAMENTO:
 *    a) Registra timestamp inicial
 *    b) Executa chamada HTTP GET para o endpoint configurado
 *    c) Calcula tempo de resposta
 *    d) Classifica o status:
 *       - UP para respostas 2xx
 *       - DOWN para outros status codes ou erros
 * 
 * 3. SAÍDA:
 *    - Objeto Health contendo:
 *      * Status (UP/DOWN)
 *      * Detalhes do serviço monitorado
 *      * Status code HTTP (quando aplicável)
 *      * Tempo de resposta
 *      * Detalhes de erro (quando ocorre exceção)
 * 
 * ========================================================
 * RELACIONAMENTOS COM OUTRAS CLASSES
 * ========================================================
 * 
 * +---------------------+-------------------+-----------------------------------+
 * | Classe              | Tipo de Relação   | Descrição                         |
 * +---------------------+-------------------+-----------------------------------+
 * | HealthIndicator     | Implementação     | Interface contrato do Spring      |
 * | RestTemplate        | Dependência       | Cliente HTTP para chamadas REST   |
 * | Health              | Retorno           | Objeto de resposta do Actuator    |
 * | HttpStatus          | Utilização        | Enum de status HTTP               |
 * | Duration/Instant    | Utilização        | Cálculo de tempo de resposta      |
 * +---------------------+-------------------+-----------------------------------+
 */