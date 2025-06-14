/* ========================================================
# Classe: AdminClientController
# Módulo: pagamento-common-health
# Autor: William Silva
# Tecnologias: Java 8, Spring Boot 2.7
# Descrição: Controller para endpoints administrativos de health check
# ======================================================== */

package com.pagamento.common.controller;

import org.springframework.boot.actuate.health.HealthComponent;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints administrativos para health check e monitoramento")
public class AdminClientController {

    private final HealthEndpoint healthEndpoint;

    public AdminClientController(HealthEndpoint healthEndpoint) {
        this.healthEndpoint = healthEndpoint;
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health Check básico",
        description = "Verifica se a aplicação está online",
        responses = {
            @ApiResponse(responseCode = "200", description = "Aplicação online"),
            @ApiResponse(responseCode = "503", description = "Aplicação offline")
        }
    )
    public String basicHealthCheck() {
        return "Application is UP";
    }

    @GetMapping("/health/detailed")
    @Operation(
        summary = "Health Check detalhado",
        description = "Retorna informações completas sobre o status da aplicação",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status detalhado"),
            @ApiResponse(responseCode = "503", description = "Problemas detectados")
        }
    )
    public HealthComponent detailedHealthCheck() {
        return healthEndpoint.health();
    }
}
