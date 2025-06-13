/* ========================================================
# Classe: KafkaMessageListener
# Módulo: Messaging System - Kafka Message Handling
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring Kafka 2.7, Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Componente para processamento de mensagens Kafka relacionadas a pagamentos.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Mensagens recebidas dos tópicos configurados</li>
 *   <li><b>Processamento:</b> Executa lógica de negócio específica</li>
 *   <li><b>Saída:</b> Confirmação (ACK) ou tratamento de erro</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaTopics - Constantes com nomes dos tópicos</li>
 *   <li>PaymentService - Serviço de negócio para processamento</li>
 *   <li>ErrorHandler - Tratamento de erros personalizado</li>
 * </ul>
 */
@Component
@Schema(description = "Listener Kafka para processamento de mensagens de pagamento")
public class KafkaMessageListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    /**
     * Processa mensagens de pagamento criado.
     *
     * @param record Registro Kafka com mensagem
     * @param ack Controle de confirmação
     * 
     * @Operation(
     *     summary = "Processa pagamentos criados",
     *     description = "Recebe eventos de novos pagamentos criados para processamento inicial"
     * )
     * @Schema(
     *     description = "Payload de pagamento criado",
     *     example = "{\"paymentId\":\"123\", \"amount\":100.50}"
     * )
     */
    @KafkaListener(
        topics = KafkaTopics.PAYMENT_CREATED,
        groupId = "${spring.kafka.consumer.group-id}",
        concurrency = "3"
    )
    public void handlePaymentCreated(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            log.info("Processando pagamento criado: key={}, offset={}, partition={}", 
                   record.key(), record.offset(), record.partition());
            
            // TODO: Implementar lógica de processamento
            processPaymentCreation(record.value());
            
            ack.acknowledge();
            log.debug("Pagamento processado com sucesso: {}", record.key());
        } catch (Exception e) {
            log.error("Falha ao processar pagamento criado. key={}, partition={}, offset={}. Erro: {}", 
                    record.key(), record.partition(), record.offset(), e.getMessage(), e);
            // TODO: Implementar Dead Letter Queue ou retry policy
        }
    }

    /**
     * Processa mensagens de pagamento processado.
     *
     * @param record Registro Kafka com mensagem
     * @param ack Controle de confirmação
     * 
     * @Operation(
     *     summary = "Processa pagamentos concluídos",
     *     description = "Recebe eventos de pagamentos processados para atualização de status"
     * )
     */
    @KafkaListener(
        topics = KafkaTopics.PAYMENT_PROCESSED,
        groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handlePaymentProcessed(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        log.info("Recebido pagamento processado: key={}", record.key());
        
        try {
            // TODO: Implementar lógica de atualização
            updatePaymentStatus(record.value());
            
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Erro atualizando status do pagamento: {}", e.getMessage(), e);
            // TODO: Implementar estratégia de retry
        }
    }

    private void processPaymentCreation(Object paymentData) {
        // Implementação do processamento
    }

    private void updatePaymentStatus(Object paymentData) {
        // Implementação da atualização
    }
}