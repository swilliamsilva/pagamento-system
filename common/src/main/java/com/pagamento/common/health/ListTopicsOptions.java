/* ========================================================
# Classe: ListTopicsOptions
# Módulo: Common Health Check - Kafka Health Indicator
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7 e Maven - Junho de 2025
# ======================================================== */
package com.pagamento.common.health;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.kafka.common.requests.MetadataRequest;

@Schema(description = "Opções para listagem de tópicos Kafka no health check")
public class ListTopicsOptions {

    @Schema(description = "Timeout para operação de listagem de tópicos (ms)", 
            example = "5000", 
            defaultValue = "5000")
    private long timeoutMs = 5000;

    @Schema(description = "Se deve incluir tópicos internos do Kafka", 
            example = "false", 
            defaultValue = "false")
    private boolean includeInternalTopics = false;

    @Schema(description = "Lista de tópicos específicos para verificação (vazio = todos)", 
            example = "[\"pagamentos\", \"transacoes\"]", 
            defaultValue = "[]")
    private String[] topicNames = {};

    // Getters and Setters
    public long getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public boolean isIncludeInternalTopics() {
        return includeInternalTopics;
    }

    public void setIncludeInternalTopics(boolean includeInternalTopics) {
        this.includeInternalTopics = includeInternalTopics;
    }

    public String[] getTopicNames() {
        return topicNames;
    }

    public void setTopicNames(String[] topicNames) {
        this.topicNames = topicNames;
    }

    /**
     * Retorna os nomes dos tópicos para verificação
     */
    public String[] getTopicsForVerification() {
        return topicNames != null ? topicNames.clone() : new String[0];
    }
}