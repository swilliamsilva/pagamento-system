package com.pagamento.common.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, String key, Object message) {
        kafkaTemplate.send(topic, key, message);
    }

    public CompletableFuture<SendResult<String, Object>> sendMessageWithCallback(
        String topic, 
        Object message
    ) {
        return kafkaTemplate.send(topic, message)
            .completable()
            .thenApply(result -> {
                // Log de sucesso ou métricas
                return result;
            })
            .exceptionally(ex -> {
                // Tratamento de erro e métricas
                return null;
            });
    }
}
