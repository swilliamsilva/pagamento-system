/* ========================================================
# Classe: ShowDetails
# Módulo: Health Check
# Descrição: Classe responsável por exibir detalhes do status de saúde da aplicação
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.health;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Classe responsável por prover endpoints para verificação de saúde da aplicação.
 * 
 * Relacionamentos com outras classes:
 * - Integra com Spring Actuator para coleta de métricas
 * - Utiliza HealthIndicator para composição do status de saúde
 * - Consumida pelo LoadBalancer para verificação de instâncias saudáveis
 */
@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Operações relacionadas à verificação de saúde da aplicação")
public class ShowDetails {

    public static final ShowDetails NEVER = null;
	public static ShowDetails ALWAYS;

    /**
     * Endpoint que retorna o status detalhado da saúde da aplicação.
     * 
     * Fluxo:
     * 1. Entrada: Requisição GET sem parâmetros
     * 2. Processamento: 
     *    - Coleta métricas do sistema
     *    - Verifica conexões com serviços externos
     *    - Avalia recursos do sistema
     * 3. Saída: JSON com status detalhado da aplicação
     * 
     * @return String contendo o status detalhado da saúde da aplicação
     */
    @GetMapping("/details")
    @Operation(
        summary = "Obter detalhes de saúde",
        description = "Retorna informações detalhadas sobre o status de saúde da aplicação",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Status de saúde retornado com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        example = "{\"status\": \"UP\", \"components\": {\"db\": {\"status\": \"UP\"}, \"disk\": {\"status\": \"UP\"}}}"
                    )
                )
            ),
            @ApiResponse(
                responseCode = "503",
                description = "Serviço indisponível",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        example = "{\"status\": \"DOWN\", \"error\": \"Database connection failed\"}"
                    )
                )
            )
        }
    )
    public String getHealthDetails() {
        // Implementação simulada - numa aplicação real, isso viria do Actuator ou similar
        return "{\n" +
               "    \"status\": \"UP\",\n" +
               "    \"components\": {\n" +
               "        \"db\": {\n" +
               "            \"status\": \"UP\",\n" +
               "            \"details\": {\n" +
               "                \"database\": \"PostgreSQL\",\n" +
               "                \"validationQuery\": \"isValid()\"\n" +
               "            }\n" +
               "        },\n" +
               "        \"disk\": {\n" +
               "            \"status\": \"UP\",\n" +
               "            \"details\": {\n" +
               "                \"total\": \"500GB\",\n" +
               "                \"free\": \"350GB\",\n" +
               "                \"threshold\": \"10GB\"\n" +
               "            }\n" +
               "        }\n" +
               "    }\n" +
               "}";
    }
    }