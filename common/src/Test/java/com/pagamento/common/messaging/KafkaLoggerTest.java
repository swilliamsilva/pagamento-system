package com.pagamento.common.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * Logger estruturado para mensagens Kafka com suporte a MDC (Mapped Diagnostic Context).
 */
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
        withContext(fromRecord(record), () -> {
            delegate.info("[KAFKA] {} - topic={}, partition={}, offset={}, key={}",
                    message, record.topic(), record.partition(), record.offset(), record.key());
        });
    }

    public void info(String message, Object key, Object value) {
        Map<String, String> context = new HashMap<>();
        context.put(KEY, String.valueOf(key));
        withContext(context, () -> {
            delegate.info("[APP] {} - key={}, value={}", message, key, value);
        });
    }

    public void error(String message, String topic, Integer partition, Long offset, Object key, Exception exception) {
        Map<String, String> context = new HashMap<>();
        context.put(TOPIC, topic);
        context.put(PARTITION, String.valueOf(partition));
        context.put(OFFSET, String.valueOf(offset));
        context.put(KEY, String.valueOf(key));
        context.put(EXCEPTION, exception.getClass().getName());

        withContext(context, () -> {
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

    private Map<String, String> fromRecord(ConsumerRecord<?, ?> record) {
        Map<String, String> context = new HashMap<>();
        context.put(TOPIC, record.topic());
        context.put(PARTITION, String.valueOf(record.partition()));
        context.put(OFFSET, String.valueOf(record.offset()));
        context.put(KEY, String.valueOf(record.key()));
        return context;
    }

    public void debug(String message, ConsumerRecord<?, ?> record) {
        if (delegate.isDebugEnabled()) {
            withContext(fromRecord(record), () -> {
                delegate.debug("[KAFKA-DEBUG] {}", message);
            });
        }
    }

    public void warn(String message, ConsumerRecord<?, ?> record, Exception e) {
        withContext(fromRecord(record), () -> {
            delegate.warn("[KAFKA-WARN] {} - {}", message, e.getMessage(), e);
        });
    }
}
