package com.pagamento.common.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessageListener.class);

    @KafkaListener(topics = KafkaTopics.PAYMENT_CREATED)
    public void handlePaymentCreated(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        try {
            log.info("Processando pagamento criado: key={}, value={}", record.key(), record.value());
            // Lógica de processamento...
            ack.acknowledge(); // Confirma processamento
        } catch (Exception e) {
            log.error("Erro processando pagamento criado", e, e, e, e, e);
            // Não confirma para reprocessamento
        }
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_PROCESSED)
    public void handlePaymentProcessed(ConsumerRecord<String, Object> record, Acknowledgment ack) {
        log.info("Pagamento processado: key={}, value={}", record.key(), record.value());
        ack.acknowledge();
    }
}
