package com.pagamento.common.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ========================================================
 * DOCUMENTAÇÃO OPENAPI - CONTROLLER DE DOCUMENTAÇÃO
 * 
 * Classe responsável por servir a especificação OpenAPI em formato YAML
 * Módulo: pagamento-common
 * Autor: William Silva
 * Contato: williamsilva.codigo@gmail.com
 * Site: simuleagora.com
 * ========================================================
 * 
 * Fluxo:
 * 1. Entrada:
 *    - Requisição GET para /api-docs/external-health.yaml
 * 
 * 2. Processamento:
 *    - Localiza o arquivo YAML no classpath (static/api-docs/)
 *    - Configura o tipo de conteúdo como application/yaml
 * 
 * 3. Saída:
 *    - Retorna o arquivo YAML com a especificação OpenAPI
 * 
 * Relacionamentos:
 * - OpenApiConfig: Classe de configuração principal
 * - SpringDocConfig: Configuração do SpringDoc
 * - Arquivo static/api-docs/external-health.yaml: Especificação em YAML
 */
@RestController
public class OpenApiController {
    
    /**
     * Endpoint para servir a especificação OpenAPI em formato YAML
     * 
     * @return Resource contendo o arquivo YAML
     * 
     * @operationId getOpenApiSpec
     * @summary Obtém a especificação OpenAPI em YAML
     * @description Retorna o arquivo YAML com a documentação completa da API
     * 
     * @response 200 OK - Especificação YAML retornada com sucesso
     * @response 404 Not Found - Arquivo YAML não encontrado
     */
    @GetMapping(value = "/api-docs/external-health.yaml", produces = "application/yaml")
    public Resource getOpenApiSpec() {
        return new ClassPathResource("static/api-docs/external-health.yaml");
    }
}