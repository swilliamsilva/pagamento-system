package com.pagamento.common.config.db;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ========================================================
 * DOCUMENTAÇÃO OPENAPI - CONFIGURAÇÃO DE DOCUMENTAÇÃO DA API
 * 
 * Classe responsável pela configuração da documentação OpenAPI/Swagger
 * Módulo: pagamento-common
 * Autor: William Silva
 * Contato: williamsilva.codigo@gmail.com
 * Site: simuleagora.com
 * ========================================================
 * 
 * Fluxo:
 * 1. Entrada:
 *    - Requisições HTTP para endpoints documentados
 *    - Acesso à UI do Swagger (/swagger-ui.html)
 * 
 * 2. Processamento:
 *    - Configura grupos de endpoints para documentação
 *    - Define metadados da API (título, versão, descrição)
 *    - Configura documentação externa complementar
 * 
 * 3. Saída:
 *    - Geração automática da documentação OpenAPI
 *    - Disponibilização via endpoints (/v3/api-docs)
 *    - UI interativa do Swagger
 * 
 * Relacionamentos:
 * - SpringDocAutoConfiguration (dependência automática)
 * - SwaggerUI (interface interativa)
 * - Controladores com anotações @Operation
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configura o grupo de endpoints para health checks
     * 
     * @return Configuração do grupo "health-checks"
     * 
     * Endpoints incluídos:
     * - /actuator/health/**
     * 
     * Documentação gerada:
     * - Título: "API de Health Checks"
     * - Descrição: "Monitoramento de serviços externos"
     * - Versão: "1.0"
     */
    @Bean
    public GroupedOpenApi healthApi() {
        return GroupedOpenApi.builder()
                .group("health-checks")
                .pathsToMatch("/actuator/health/**")
                .addOpenApiCustomizer(openApi -> {
                    openApi.getInfo().setTitle("API de Health Checks");
                    openApi.getInfo().setDescription(
                        "Monitoramento de status de serviços externos\n" +
                        "========================================\n" +
                        "Fornece endpoints para verificar a saúde de:\n" +
                        "- APIs de parceiros\n" +
                        "- Bancos de dados\n" +
                        "- Serviços de mensageria\n" +
                        "========================================");
                    openApi.getInfo().setVersion("1.0");
                })
                .build();
    }

    /**
     * Configuração global da documentação OpenAPI
     * 
     * @return Configuração principal da API
     * 
     * Metadados incluídos:
     * - Título: "Pagamento System API"
     * - Contato: William Silva
     * - Documentação externa
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Pagamento System API")
                .description(
                    "API do Sistema de Pagamentos\n" +
                    "========================================\n" +
                    "Health Check de Serviços Externos\n" +
                    "Módulo: pagamento-common\n" +
                    "========================================")
                .version("1.0")
                .contact(new Contact()
                    .name("William Silva")
                    .email("williamsilva.codigo@gmail.com")
                    .url("https://simuleagora.com")))
            .externalDocs(new ExternalDocumentation()
                .description("Documentação Completa")
                .url("https://api.simuleagora.com/docs"));
    }
}