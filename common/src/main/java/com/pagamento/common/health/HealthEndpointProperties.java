/* ========================================================
# Classe: HealthEndpointProperties
# Módulo: Common Health Check
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.health;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Classe de propriedades para configuração do endpoint de health check.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Propriedades são carregadas do arquivo application.yml</li>
 *   <li><b>Processamento:</b> Spring Boot configura as propriedades durante a inicialização</li>
 *   <li><b>Saída:</b> As propriedades são utilizadas pelo HealthEndpoint para configurar o comportamento do endpoint</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>HealthEndpoint - Endpoint principal que utiliza estas propriedades</li>
 *   <li>HealthIndicator - Componentes que verificam a saúde da aplicação</li>
 *   <li>HealthEndpointAutoConfiguration - Configuração automática do Spring</li>
 * </ul>
 */
@Schema(description = "Configurações do endpoint de verificação de saúde da aplicação")
public class HealthEndpointProperties {

    @Schema(description = "Habilita ou desabilita o endpoint de health check", 
            example = "true", 
            defaultValue = "true")
    private boolean enabled = true;

    @Schema(description = "Mostra detalhes completos da verificação de saúde", 
            example = "WHEN_AUTHORIZED", 
            allowableValues = {"ALWAYS", "NEVER", "WHEN_AUTHORIZED"}, 
            defaultValue = "WHEN_AUTHORIZED")
    private String showDetails = "WHEN_AUTHORIZED";

    @Schema(description = "Mostra componentes individuais de saúde", 
            example = "true", 
            defaultValue = "true")
    private boolean showComponents = true;

    @Schema(description = "Roles permitidas para visualizar detalhes completos", 
            example = "[\"ADMIN\"]", 
            defaultValue = "[\"ADMIN\"]")
    private String[] roles = new String[]{"ADMIN"};

    // Getters e Setters

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getShowDetails() {
        return showDetails;
    }

    public void setShowDetails(String showDetails) {
        this.showDetails = showDetails;
    }

    public boolean isShowComponents() {
        return showComponents;
    }

    public void setShowComponents(boolean showComponents) {
        this.showComponents = showComponents;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}