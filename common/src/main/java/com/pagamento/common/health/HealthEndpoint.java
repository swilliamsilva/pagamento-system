package com.pagamento.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthContributor;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.NamedContributor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Endpoint customizado para exposição de informações de saúde da aplicação.
 */
@Component
@Endpoint(id = "health")
@Tag(
    name = "Application Health",
    description = "Endpoint para monitoramento da saúde da aplicação",
    externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
        description = "Spring Boot Actuator Health Reference",
        url = "https://docs.spring.io/spring-boot/docs/current/actuator-api/htmlsingle/#health"
    )
)
public class HealthEndpoint {

    private final HealthContributorRegistry contributorRegistry;

    public HealthEndpoint(HealthContributorRegistry contributorRegistry) {
        this.contributorRegistry = contributorRegistry;
    }

    @ReadOperation
    @Operation(
        summary = "Health Check completo",
        description = "Endpoint principal para verificação de saúde da aplicação",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Aplicação saudável",
                content = @Content(
                    mediaType = "application/vnd.spring-boot.actuator.v3+json",
                    schema = @Schema(implementation = Health.class),
                    examples = @ExampleObject(
                        value = "{\"status\": \"UP\", \"components\": {\"db\": {\"status\": \"UP\", \"details\": {\"database\": \"PostgreSQL\", \"validationQuery\": \"isValid()\"}}, \"diskSpace\": {\"status\": \"UP\", \"details\": {\"total\": 500107862016, \"free\": 313857421312, \"threshold\": 10485760}}}}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "503",
                description = "Aplicação com problemas",
                content = @Content(
                    mediaType = "application/vnd.spring-boot.actuator.v3+json",
                    examples = @ExampleObject(
                        value = "{\"status\": \"DOWN\", \"components\": {\"db\": {\"status\": \"DOWN\", \"details\": {\"error\": \"Connection refused: localhost:5432\"}}}}"
                    )
                )
            )
        }
    )
    public Health health() {
        Map<String, Health> healths = new HashMap<>();
        contributorRegistry.stream()
            .filter(nc -> nc.getContributor() instanceof HealthIndicator)
            .forEach(nc -> {
                String name = nc.getName();
                HealthIndicator indicator = (HealthIndicator) nc.getContributor();
                healths.put(name, indicator.health());
            });

        Status status = healths.values().stream()
            .anyMatch(h -> h.getStatus().equals(Status.DOWN)) ? Status.DOWN : Status.UP;
        return Health.status(status).withDetails(healths).build();
    }

    @Operation(hidden = true)
    public HealthContributorRegistry getContributorRegistry() {
        return this.contributorRegistry;
    }
}
