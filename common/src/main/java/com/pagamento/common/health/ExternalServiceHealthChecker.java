package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/**
 * Health Checker para serviços externos que implementa HealthIndicator do Spring Boot Actuator.
 * 
 * @author William Silva - williamsilva.codigo@gmail.com
 * @version 1.0
 * @see HealthIndicator
 */
@Component("externalServiceHealthIndicator")
public class ExternalServiceHealthChecker implements HealthIndicator {

    private final RestTemplate restTemplate;
    
    @Value("${external.service.health.url:https://api.bancoexterno.com/health}")
    private String healthUrl;
    
    @Value("${external.service.name:BancoExternoAPI}")
    private String serviceName;

    /**
     * Construtor para injeção de dependência do RestTemplate
     * 
     * @param restTemplate Client HTTP para chamadas REST
     */
    public ExternalServiceHealthChecker(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Implementação do health check para serviços externos
     * 
     * @return Health objeto contendo o status do serviço monitorado
     */
    @Override
    public Health health() {
        Instant start = Instant.now();
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(healthUrl, String.class);
            long responseTime = Duration.between(start, Instant.now()).toMillis();
            HttpStatus statusCode = response.getStatusCode();

            Health.Builder builder = statusCode.is2xxSuccessful() ? Health.up() : Health.down();
            
            return builder
                .withDetail("service", serviceName)
                .withDetail("status", statusCode.value())
                .withDetail("response_time_ms", responseTime)
                .build();
                
        } catch (Exception ex) {
            long responseTime = Duration.between(start, Instant.now()).toMillis();
            return Health.down()
                .withDetail("service", serviceName)
                .withDetail("exception", ex.getClass().getSimpleName())
                .withDetail("message", ex.getMessage())
                .withDetail("response_time_ms", responseTime)
                .build();
        }
    }
}

/*
 * Fluxo de Operação (Passo a Passo)
1. Entrada
    Trigger: Chamada ao endpoint de health check do Spring Boot Actuator
    Parâmetros Configuráveis:
        external.service.health.url: URL do endpoint de health check do
         serviço externo
        external.service.name: Nome do serviço sendo monitorado

2. Processamento
    Registra o tempo inicial (Instant.now())
    Faz uma requisição GET para o endpoint de health check do
     serviço externo
     Calcula o tempo de resposta (Duration.between)
     Verifica o status code da resposta:

        2xx: Considera o serviço UP
        Outros: Considera o serviço DOWN

    Em caso de exceção:

        Captura detalhes da exceção
        Considera o serviço DOWN

3. Saída

    Formato: Objeto Health do Spring Boot Actuator
    Detalhes Incluídos:
        Status (UP/DOWN)
        Nome do serviço
        Status HTTP (quando disponível)
        Tempo de resposta em ms
        Detalhes de erro (quando aplicável)

Relacionamentos com outras Classes
Classe	Tipo de Relação	Descrição
RestTemplate	Dependência	Cliente HTTP para fazer chamadas ao serviço externo
HealthIndicator	Implementação	Interface do Spring Boot Actuator para health checks
Health	Retorno	Classe que representa o resultado do health check
HttpStatus	Utilização	Enum com códigos de status HTTP
 * 
 * **/
 */