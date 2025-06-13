package com.pagamento.common.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Logger especializado para mensageria Kafka")
public final class KafkaLogger {

    private static final String TOPIC = "topic";
    private static final String PARTITION = "partition";
    private static final String OFFSET = "offset";
    private static final String KEY = "messageKey";
    private static final String EXCEPTION = "exception";

    private final Logger delegate;

    // Construtor principal para produção
    public KafkaLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    // Construtor alternativo para testes (injeção do mock)
    public KafkaLogger(Logger logger) {
        this.delegate = logger;
    }

    public void info(String message, ConsumerRecord<?, ?> record) {
        withContext(record, () -> {
            delegate.info("[KAFKA] {} - topic={}, partition={}, offset={}, key={}", 
                message, record.topic(), record.partition(), record.offset(), record.key());
        });
    }

    public void info(String message, Object key, Object value) {
        withContext(Map.of(KEY, String.valueOf(key)), () -> {
            delegate.info("[APP] {} - key={}, value={}", message, key, value);
        });
    }

    public void error(String message, String topic, Integer partition, Long offset, Object key,
            Exception exception) {
        withContext(Map.of(
            TOPIC, topic,
            PARTITION, String.valueOf(partition),
            OFFSET, String.valueOf(offset),
            KEY, String.valueOf(key),
            EXCEPTION, exception.getClass().getName()
        ), () -> {
            delegate.error("[KAFKA-ERROR] {} - topic={}, partition={}, offset={}, key={}", 
                message, topic, partition, offset, key, exception);
        });
    }

    @Deprecated
    public void error(String message, Exception... exceptions) {
        if (exceptions.length > 0) {
            Exception primary = exceptions[0];
            delegate.error("[LEGACY] {} - rootCause={}", message, primary.getMessage(), primary);
        }
    }

    @Deprecated
    public void error1(String message, Exception... exceptions) {
        error(message, exceptions);
    }

    private void withContext(Map<String, String> context, Runnable action) {
        try {
            context.forEach(MDC::put);
            action.run();
        } finally {
            context.keySet().forEach(MDC::remove);
        }
    }

    private void withContext(ConsumerRecord<?, ?> record, Runnable action) {
        withContext(Map.of(
            TOPIC, record.topic(),
            PARTITION, String.valueOf(record.partition()),
            OFFSET, String.valueOf(record.offset()),
            KEY, String.valueOf(record.key())
        ), action);
    }

    public void debug(String message, ConsumerRecord<?, ?> record) {
        if (delegate.isDebugEnabled()) {
            withContext(record, () -> {
                delegate.debug("[KAFKA-DEBUG] {}", message);
            });
        }
    }

    public void warn(String message, ConsumerRecord<?, ?> record, Exception e) {
        withContext(record, () -> {
            delegate.warn("[KAFKA-WARN] {} - {}", message, e.getMessage(), e);
        });
    }
}
