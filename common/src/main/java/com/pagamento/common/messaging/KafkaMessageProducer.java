package com.pagamento.common.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaMessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }

    public void sendMessage(String topic, String key, Object message) {
        kafkaTemplate.send(topic, key, message);
    }

    public void sendMessageWithCallback(String topic, Object message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                // Log de sucesso ou métricas
            }

            @Override
            public void onFailure(Throwable ex) {
                // Tratamento de erro e métricas
            }
        });
    }

    public void sendMessageSynchronous(String topic, Object message) 
            throws InterruptedException, ExecutionException, TimeoutException {
        kafkaTemplate.send(topic, message).get(5, TimeUnit.SECONDS);
    }
}
