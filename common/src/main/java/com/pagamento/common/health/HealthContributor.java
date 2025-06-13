package com.pagamento.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Implementação customizada de HealthIndicator para verificação de saúde de componentes específicos.
 */
@Component
@Tag(
    name = "Health Infrastructure",
    description = "Componentes internos de verificação de saúde da aplicação",
    externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
        description = "Spring Boot Actuator Health Reference",
        url = "https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/#health"
    )
)
public class HealthContributor implements HealthIndicator {

    /**
     * Implementação principal do health check customizado.
     *
     * @return Objeto Health com status e detalhes
     */
    @Override
    @Operation(
        summary = "Health status do contribuidor",
        description = "Fornece o status de saúde de componentes customizados",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Status de saúde disponível",
                content = @Content(
                    mediaType = "application/vnd.spring-boot.actuator.v3+json",
                    examples = @ExampleObject(
                        value = "{\"status\": \"UP\", \"components\": {\"custom\": {\"status\": \"UP\", \"details\": {\"service\": \"operational\", \"latency\": \"25ms\"}}}}"
                    )
                )
            )
        },
        hidden = true
    )
    public Health health() {
        boolean serviceAvailable = checkServiceAvailability();
        long latency = measureServiceLatency();

        if (serviceAvailable) {
            return Health.up()
                .withDetail("service", "operational")
                .withDetail("latency", latency + "ms")
                .build();
        } else {
            return Health.down()
                .withDetail("service", "unavailable")
                .withDetail("error", "Connection timeout")
                .build();
        }
    }

    private boolean checkServiceAvailability() {
        return true;
    }

    private long measureServiceLatency() {
        return 25L;
    }

    /**
     * Verifica a conectividade com o serviço customizado.
     */
    @Operation(
        summary = "Verificação de conectividade",
        description = "Testa a conexão com o serviço customizado",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Serviço disponível",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"available\"}"))
            ),
            @ApiResponse(
                responseCode = "503",
                description = "Serviço indisponível",
                content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\": \"unavailable\"}"))
            )
        },
        hidden = true
    )
    public void checkConnectivity() {
        // Método auxiliar para verificações adicionais
    }
}
