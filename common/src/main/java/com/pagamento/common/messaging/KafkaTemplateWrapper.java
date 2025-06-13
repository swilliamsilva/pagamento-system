package com.pagamento.common.messaging;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Wrapper simplificado para KafkaTemplate original.
 */
public class KafkaTemplateWrapper<K, V> {

    private final KafkaOperations<K, V> delegate;

    public KafkaTemplateWrapper(KafkaOperations<K, V> delegate) {
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

    public void executeInTransaction(KafkaOperations.OperationsCallback<K, V> callback) {
        delegate.executeInTransaction(callback);
    }

    public boolean isTransactional() {
        return delegate.isTransactional();
    }

    public List<PartitionInfo> partitionsFor(String topic) {
        return delegate.partitionsFor(topic);
    }

    public Map<String, ?> metrics() {
        return delegate.metrics();
    }

    public <T> T execute(KafkaOperations.ProducerCallback<K, V, T> callback) {
        return delegate.execute(callback);
    }

    public ListenableFuture<SendResult<K, V>> sendWithCallback(String topic, V message) {
        return delegate.send(topic, message).completable();
    }

    public void sendWithTimestamp(String topic, K key, V message, long timestamp) {
        ProducerRecord<K, V> record = new ProducerRecord<>(topic, null, timestamp, key, message);
        delegate.send(record);
    }

    @Transactional
    public void sendTransactional(String topic, V message) {
        delegate.executeInTransaction(t -> {
            t.send(topic, message);
            return null;
        });
    }

    public static class KafkaSendException extends RuntimeException {
        public KafkaSendException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
