package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Schema(description = "Template para envio de mensagens Kafka com suporte a diferentes estratégias")
public class KafkaTemplateWrapper<K, V> {

    private final org.springframework.kafka.core.KafkaTemplate<K, V> delegate;

    public KafkaTemplateWrapper(org.springframework.kafka.core.KafkaTemplate<K, V> delegate) {
        this.delegate = delegate;
    }

    @Transactional
    public ListenableFuture<SendResult<K, V>> send(String topic, V data) {
        return delegate.send(topic, data);
    }

    @Transactional
    public ListenableFuture<SendResult<K, V>> send(String topic, K key, V data) {
        return delegate.send(topic, key, data);
    }

    public ListenableFuture<SendResult<K, V>> send(ProducerRecord<K, V> record) {
        return delegate.send(record);
    }

    public void flush() {
        delegate.flush();
    }

    public void sendOffsetsToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId) {
        delegate.sendOffsetsToTransaction(offsets, consumerGroupId);
    }

    /**
     * Envia dentro de contexto transacional (Kafka TX).
     */
    @Transactional
    public void sendTransactional(String topic, V message) {
        delegate.executeInTransaction(template -> {
            template.send(topic, message);
            return null;
        });
    }

    public List<PartitionInfo> partitionsFor(String topic) {
        return delegate.partitionsFor(topic);
    }

    public <T> T execute(ProducerFactory<K, V> pf, org.springframework.kafka.core.KafkaOperations.ProducerCallback<K, V, T> cb) {
        return delegate.execute(pf, cb);
    }

    public Map<org.apache.kafka.common.MetricName, ? extends org.apache.kafka.common.Metric> metrics() {
        return delegate.metrics();
    }

    public CompletableFuture<SendResult<K, V>> sendWithCallback(String topic, V message) {
        ListenableFuture<SendResult<K, V>> future = send(topic, message);
        CompletableFuture<SendResult<K, V>> completable = new CompletableFuture<>();
        future.addCallback(completable::complete, completable::completeExceptionally);
        return completable;
    }

    public void sendWithTimestamp(String topic, K key, V message, long timestamp) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, null, timestamp, key, message);
        send(record);
    }

    public static class KafkaSendException extends RuntimeException {
        public KafkaSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
