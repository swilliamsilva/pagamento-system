package com.pagamento.common.messaging;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.listener.ErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaErrorHandler implements ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(KafkaErrorHandler.class);

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer) {
        log.error("Erro processando mensagem: topic={}, partition={}, offset={}, key={}",
            record.topic(), record.partition(), record.offset(), record.key(),
            thrownException);
        
        // Lógica para dead letter queue
        sendToDlq(record, thrownException, consumer);
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> record) {
        log.error("Erro sem acesso ao consumer: topic={}, partition={}, offset={}",
            record.topic(), record.partition(), record.offset(), thrownException, thrownException);
    }

    private void sendToDlq(ConsumerRecord<?, ?> record, Exception exception, Consumer<?, ?> consumer) {
        try {
            // Enviar mensagem para DLQ
            // (Implementação real requereria um KafkaTemplate)
            log.info("Enviando mensagem para DLQ: {}", record);
            
            // Commit da mensagem original após enviar para DLQ
            TopicPartition topicPartition = new TopicPartition(record.topic(), record.partition());
            consumer.seek(topicPartition, record.offset() + 1);
        } catch (Exception e) {
            log.error("Falha ao enviar para DLQ", e);
        }
    }
}
