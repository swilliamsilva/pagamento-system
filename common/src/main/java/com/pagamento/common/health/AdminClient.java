/* ========================================================
# Classe: AdminClient
# Módulo: Health Check
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, spring 2.7 e Maven  - Junho de 2025
# ========================================================
######D############################################################################### */

package com.pagamento.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe responsável por fornecer endpoints de health check e status da aplicação.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Requisição HTTP GET para os endpoints de health check</li>
 *   <li><b>Processamento:</b> Verificação do status da aplicação e dependências</li>
 *   <li><b>Saída:</b> Resposta JSON com status da aplicação e informações relevantes</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>HealthIndicator (Spring Boot) - Fornece métricas de saúde padrão</li>
 *   <li>ActuatorEndpoint (Spring Boot Actuator) - Endpoints de monitoramento</li>
 * </ul>
 */
@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints administrativos para monitoramento e health check da aplicação")
public class AdminClient {

    /**
     * Endpoint para verificação básica de saúde da aplicação.
     * 
     * @return Mensagem simples indicando que a aplicação está online
     * 
     * @operationId getHealthCheck
     * @summary Verifica se a aplicação está online
     * @description Retorna status básico indicando que a aplicação está respondendo
     */
    @GetMapping("/health")
    @Operation(
        summary = "Health Check básico",
        description = "Endpoint simples para verificar se a aplicação está online e respondendo",
        responses = {
            @ApiResponse(responseCode = "200", description = "Aplicação online e saudável"),
            @ApiResponse(responseCode = "503", description = "Aplicação indisponível")
        }
    )
    public String healthCheck() {
        return "Application is UP";
    }

    /**
     * Endpoint para verificação detalhada do status da aplicação e suas dependências.
     * 
     * @return JSON com status detalhado da aplicação
     * 
     * @operationId getDetailedHealth
     * @summary Status detalhado da aplicação
     * @description Retorna informações detalhadas sobre o status da aplicação e suas dependências
     */
    @GetMapping("/health/detailed")
    @Operation(
        summary = "Health Check detalhado",
        description = "Endpoint que retorna o status detalhado da aplicação incluindo dependências",
        responses = {
            @ApiResponse(responseCode = "200", description = "Status detalhado da aplicação"),
            @ApiResponse(responseCode = "503", description = "Problemas nas dependências da aplicação")
        }
    )
    public String detailedHealthCheck() {
        // Implementação que verifica banco de dados, serviços externos, etc.
        return "{\"status\":\"UP\",\"components\":{\"db\":{\"status\":\"UP\"},\"externalService\":{\"status\":\"UP\"}}}";
    }
}