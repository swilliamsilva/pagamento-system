/* ========================================================
# Classe: KafkaMessageProducer
# Módulo: Messaging System - Kafka Message Production
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring Kafka 2.7, Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.CompletableFuture;

/**
 * Componente para produção de mensagens Kafka com suporte a diferentes estratégias de envio.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Dados da mensagem e tópico destino</li>
 *   <li><b>Processamento:</b> Serialização e envio para o broker Kafka</li>
 *   <li><b>Saída:</b> Confirmação de recebimento ou tratamento de erro</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaTemplate - Template Spring para operações Kafka</li>
 *   <li>ProducerFactory - Configuração do produtor subjacente</li>
 *   <li>KafkaTopics - Constantes com nomes dos tópicos</li>
 * </ul>
 */
@Component
@Schema(description = "Produtor de mensagens Kafka para o sistema de pagamentos")
public class KafkaMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Construtor com injeção de dependência.
     * 
     * @param kafkaTemplate Template Kafka configurado
     */
    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Envia mensagem sem chave específica.
     * 
     * @param topic Tópico destino
     * @param message Conteúdo da mensagem
     * 
     * @Operation(summary = "Envia mensagem sem chave específica")
     * @Schema(description = "Mensagem a ser enviada", required = true)
     */
    public void sendMessage(String topic, Object message) {
        log.debug("Enviando mensagem para tópico {}: {}", topic, message);
        kafkaTemplate.send(topic, message)
            .addCallback(
                result -> log.debug("Mensagem enviada com sucesso para tópico {}", topic),
                ex -> log.error("Falha ao enviar mensagem para tópico {}", topic, ex)
            );
    }

    /**
     * Envia mensagem com chave para particionamento.
     * 
     * @param topic Tópico destino
     * @param key Chave para particionamento
     * @param message Conteúdo da mensagem
     * 
     * @Operation(summary = "Envia mensagem com chave de particionamento")
     * @Schema(description = "Chave para particionamento da mensagem", required = true)
     */
    public void sendMessage(String topic, String key, Object message) {
        log.debug("Enviando mensagem com chave para tópico {}: {}={}", topic, key, message);
        kafkaTemplate.send(topic, key, message)
            .addCallback(
                result -> log.debug("Mensagem com chave enviada com sucesso para tópico {}", topic),
                ex -> log.error("Falha ao enviar mensagem com chave para tópico {}", topic, ex)
            );
    }

    /**
     * Envia mensagem com tratamento assíncrono do resultado.
     * 
     * @param topic Tópico destino
     * @param message Conteúdo da mensagem
     * @return Future para acompanhamento do resultado
     * 
     * @Operation(summary = "Envia mensagem com callback assíncrono")
     * @Schema(description = "Future para acompanhamento do envio")
     */
    public CompletableFuture<SendResult<String, Object>> sendMessageWithCallback(
        String topic, 
        Object message
    ) {
        log.debug("Enviando mensagem assíncrona para tópico {}: {}", topic, message);
        return kafkaTemplate.send(topic, message)
            .completable()
            .thenApply(result -> {
                log.info("Mensagem enviada com sucesso para tópico {} [partition={}, offset={}]", 
                    topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                // TODO: Adicionar métricas de sucesso
                return result;
            })
            .exceptionally(ex -> {
                log.error("Falha crítica ao enviar mensagem para tópico {}", topic, ex);
                // TODO: Adicionar métricas de erro e estratégia de retry
                throw new MessageProductionException("Falha ao enviar mensagem para tópico " + topic, ex);
            });
    }

    /**
     * Envia mensagem para tópico com timestamp específico.
     * 
     * @param topic Tópico destino
     * @param key Chave da mensagem
     * @param message Conteúdo da mensagem
     * @param timestamp Timestamp da mensagem
     * 
     * @Operation(summary = "Envia mensagem com timestamp controlado")
     */
    public void sendMessageWithTimestamp(
        String topic, 
        String key, 
        Object message, 
        long timestamp
    ) {
        log.debug("Enviando mensagem com timestamp para tópico {}: {}", topic, message);
        kafkaTemplate.send(topic, null, timestamp, key, message)
            .addCallback(
                result -> log.debug("Mensagem com timestamp enviada com sucesso"),
                ex -> log.error("Falha ao enviar mensagem com timestamp", ex)
            );
    }

    @Schema(description = "Exceção de falha no envio de mensagem")
    public static class MessageProductionException extends RuntimeException {
        public MessageProductionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}