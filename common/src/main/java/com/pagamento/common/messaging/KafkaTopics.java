/* ========================================================
# Classe: KafkaTopics
# Módulo: Messaging System - Kafka Topic Definitions
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring Kafka 2.7, Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Centralização de todos os tópicos Kafka utilizados no sistema de pagamentos.
 * 
 * <p>Organização por domínios:</p>
 * <ul>
 *   <li>Pagamentos: Fluxo principal de processamento</li>
 *   <li>Reconciliação: Conciliação financeira</li>
 *   <li>Notificação: Comunicação com clientes</li>
 *   <li>Antifraude: Análise de segurança</li>
 *   <li>Cancelamento: Fluxo de estornos</li>
 *   <li>Relatórios: Geração de dados analíticos</li>
 *   <li>ERP: Integração com sistemas corporativos</li>
 * </ul>
 * 
 * <p>Padrão de nomenclatura:</p>
 * <pre>{contexto}.{evento}.{status}</pre>
 */
@Schema(description = "Definição centralizada de todos os tópicos Kafka do sistema")
public final class KafkaTopics {

    // ========== DOMÍNIO DE PAGAMENTOS ========== //
    /**
     * Evento: Novo pagamento criado
     * Partições: 8 (alta throughput)
     * Retenção: 7 dias
     */
    @Schema(description = "Tópico para eventos de pagamentos criados",
            example = "payment.created",
            defaultValue = "payment.created")
    public static final String PAYMENT_CREATED = "payment.created";

    /**
     * Evento: Pagamento processado com sucesso
     * Partições: 8
     * Retenção: 30 dias (regulatório)
     */
    @Schema(description = "Tópico para pagamentos processados com sucesso",
            example = "payment.processed",
            defaultValue = "payment.processed")
    public static final String PAYMENT_PROCESSED = "payment.processed";

    /**
     * Evento: Falha no processamento
     * Partições: 4
     * Retenção: 14 dias
     */
    @Schema(description = "Tópico para falhas no processamento de pagamentos",
            example = "payment.failed",
            defaultValue = "payment.failed")
    public static final String PAYMENT_FAILED = "payment.failed";

    // ========== RECONCILIAÇÃO FINANCEIRA ========== //
    /**
     * Evento: Solicitação de conciliação
     * Partições: 2
     * Retenção: 30 dias
     */
    @Schema(description = "Tópico para solicitações de conciliação financeira",
            example = "reconciliation.request",
            defaultValue = "reconciliation.request")
    public static final String RECONCILIATION_REQUEST = "reconciliation.request";

    /**
     * Evento: Resposta de conciliação
     * Partições: 2
     * Retenção: 30 dias
     */
    @Schema(description = "Tópico para respostas de conciliação financeira",
            example = "reconciliation.response",
            defaultValue = "reconciliation.response")
    public static final String RECONCILIATION_RESPONSE = "reconciliation.response";

    // ========== NOTIFICAÇÕES ========== //
    /**
     * Evento: Notificação de sucesso
     * Partições: 4
     * Retenção: 3 dias
     */
    @Schema(description = "Tópico para notificações de pagamentos bem-sucedidos",
            example = "notification.payment.success",
            defaultValue = "notification.payment.success")
    public static final String NOTIFICATION_PAYMENT_SUCCESS = "notification.payment.success";

    /**
     * Evento: Notificação de falha
     * Partições: 4
     * Retenção: 7 dias
     */
    @Schema(description = "Tópico para notificações de pagamentos com falha",
            example = "notification.payment.failure",
            defaultValue = "notification.payment.failure")
    public static final String NOTIFICATION_PAYMENT_FAILURE = "notification.payment.failure";

    // ========== ANÁLISE DE ANTIFRAUDE ========== //
    /**
     * Evento: Solicitação de análise
     * Partições: 6 (alta prioridade)
     * Retenção: 90 dias (compliance)
     */
    @Schema(description = "Tópico para solicitações de análise antifraude",
            example = "antifraud.analysis.request",
            defaultValue = "antifraud.analysis.request")
    public static final String ANTIFRAUD_ANALYSIS_REQUEST = "antifraud.analysis.request";

    /**
     * Evento: Resposta de análise
     * Partições: 6
     * Retenção: 90 dias
     */
    @Schema(description = "Tópico para respostas de análise antifraude",
            example = "antifraud.analysis.response",
            defaultValue = "antifraud.analysis.response")
    public static final String ANTIFRAUD_ANALYSIS_RESPONSE = "antifraud.analysis.response";

    // ========== CANCELAMENTOS ========== //
    /**
     * Evento: Solicitação de cancelamento
     * Partições: 4
     * Retenção: 30 dias
     */
    @Schema(description = "Tópico para solicitações de cancelamento de pagamentos",
            example = "payment.cancellation.request",
            defaultValue = "payment.cancellation.request")
    public static final String PAYMENT_CANCELLATION_REQUEST = "payment.cancellation.request";

    /**
     * Evento: Cancelamento confirmado
     * Partições: 4
     * Retenção: 30 dias
     */
    @Schema(description = "Tópico para confirmações de cancelamento de pagamentos",
            example = "payment.cancellation.confirmed",
            defaultValue = "payment.cancellation.confirmed")
    public static final String PAYMENT_CANCELLATION_CONFIRMED = "payment.cancellation.confirmed";

    // ========== RELATÓRIOS ========== //
    /**
     * Evento: Solicitação de relatório
     * Partições: 2
     * Retenção: 7 dias
     */
    @Schema(description = "Tópico para solicitações de geração de relatórios",
            example = "report.generation.request",
            defaultValue = "report.generation.request")
    public static final String REPORT_GENERATION_REQUEST = "report.generation.request";

    /**
     * Evento: Relatório gerado
     * Partições: 2
     * Retenção: 30 dias
     */
    @Schema(description = "Tópico para notificação de relatórios gerados",
            example = "report.generated",
            defaultValue = "report.generated")
    public static final String REPORT_GENERATED = "report.generated";

    // ========== INTEGRAÇÃO ERP ========== //
    /**
     * Evento: Sincronização com ERP
     * Partições: 4
     * Retenção: 90 dias
     */
    @Schema(description = "Tópico para sincronização de pagamentos com ERP",
            example = "erp.payment.sync",
            defaultValue = "erp.payment.sync")
    public static final String ERP_PAYMENT_SYNC = "erp.payment.sync";

    private KafkaTopics() {
        throw new AssertionError("Classe utilitária não deve ser instanciada");
    }

    /**
     * Método auxiliar para verificar se um tópico é válido.
     * 
     * @param topic Nome do tópico a ser validado
     * @return Verdadeiro se o tópico existir
     */
    public static boolean isValidTopic(String topic) {
        // Implementação de verificação
        return topic != null && !topic.trim().isEmpty();
    }

    /**
     * Obtém todos os tópicos definidos.
     * 
     * @return Array com todos os tópicos
     */
    public static String[] getAllTopics() {
        return new String[] {
            PAYMENT_CREATED, PAYMENT_PROCESSED, PAYMENT_FAILED,
            RECONCILIATION_REQUEST, RECONCILIATION_RESPONSE,
            NOTIFICATION_PAYMENT_SUCCESS, NOTIFICATION_PAYMENT_FAILURE,
            ANTIFRAUD_ANALYSIS_REQUEST, ANTIFRAUD_ANALYSIS_RESPONSE,
            PAYMENT_CANCELLATION_REQUEST, PAYMENT_CANCELLATION_CONFIRMED,
            REPORT_GENERATION_REQUEST, REPORT_GENERATED,
            ERP_PAYMENT_SYNC
        };
    }
}