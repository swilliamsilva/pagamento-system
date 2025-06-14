/* ========================================================
# Classe: KafkaLogger
# Módulo: pagamento-common-messaging
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.pagamento.common.dto.LogMessageDTO;

import java.util.HashMap;
import java.util.Map;

@Schema(description = "Logger especializado para mensageria Kafka")
public final class KafkaLogger {

    private static final String TOPIC     = "topic";
    private static final String PARTITION = "partition";
    private static final String OFFSET    = "offset";
    private static final String KEY       = "messageKey";
    private static final String EXCEPTION = "exception";

    private final Logger delegate;
    private final KafkaTemplateWrapper<String, LogMessageDTO> kafkaTemplateWrapper;
    private final String kafkaTopic;

    /** Construtor padrão para produção */
    public KafkaLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    /** Construtor alternativo para testes (injeção de mock de Logger) */
    public KafkaLogger(Logger logger) {
        if (logger == null) {
            throw new IllegalArgumentException("Logger não pode ser nulo");
        }
        this.delegate = logger;
        this.kafkaTemplateWrapper = null;
        this.kafkaTopic = null;
    }

    /** Construtor com Kafka */
    public KafkaLogger(KafkaTemplateWrapper<String, LogMessageDTO> kafkaTemplateWrapper, String kafkaTopic) {
        if (kafkaTopic == null || kafkaTopic.trim().isEmpty()) {
            throw new IllegalArgumentException("Kafka topic não pode ser nulo ou vazio");
        }
        this.delegate = LoggerFactory.getLogger(KafkaLogger.class);
        this.kafkaTemplateWrapper = kafkaTemplateWrapper; // pode ser null
        this.kafkaTopic = kafkaTopic;
    }

    public void info(String message, ConsumerRecord<?, ?> record) {
        Map<String, String> context = buildContext(record);
        withContext(context, () ->
            delegate.info("[KAFKA] {} - topic={}, partition={}, offset={}, key={}",
                message,
                record.topic(),
                record.partition(),
                record.offset(),
                record.key()
            )
        );
    }

    public void info(String message, Object key, Object value) {
        Map<String, String> context = new HashMap<>();
        context.put(KEY, String.valueOf(key));
        withContext(context, () ->
            delegate.info("[APP] {} - key={}, value={}", message, key, value)
        );
    }

    public void error(String message,
                      String topic,
                      Integer partition,
                      Long offset,
                      Object key,
                      Exception exception) {

        Map<String, String> context = new HashMap<>();
        context.put(TOPIC,     String.valueOf(topic));
        context.put(PARTITION, String.valueOf(partition));
        context.put(OFFSET,    String.valueOf(offset));
        context.put(KEY,       String.valueOf(key));
        context.put(EXCEPTION, exception != null ? exception.getClass().getName() : "null");

        withContext(context, () ->
            delegate.error("[KAFKA-ERROR] {} - topic={}, partition={}, offset={}, key={}, error={}",
                message,
                topic,
                partition,
                offset,
                key,
                exception != null ? exception.getMessage() : "null",
                exception
            )
        );
    }

    /** @deprecated Uso legado. Substitua por métodos com contexto Kafka. */
    @Deprecated
    public void error(String message, Exception... exceptions) {
        if (exceptions != null && exceptions.length > 0 && exceptions[0] != null) {
            Exception primary = exceptions[0];
            delegate.error("[LEGACY] {} - rootCause={}", message, primary.getMessage(), primary);
        } else {
            delegate.error("[LEGACY] {} - exception não informada", message);
        }
    }

    /** @deprecated Alias de `error(...)` legado. */
    @Deprecated
    public void error1(String message, Exception... exceptions) {
        error(message, exceptions);
    }

    public void debug(String message, ConsumerRecord<?, ?> record) {
        if (!delegate.isDebugEnabled()) return;
        Map<String, String> context = buildContext(record);
        withContext(context, () ->
            delegate.debug("[KAFKA-DEBUG] {} - topic={}, partition={}, offset={}, key={}",
                message,
                record.topic(),
                record.partition(),
                record.offset(),
                record.key()
            )
        );
    }

    public void warn(String message, ConsumerRecord<?, ?> record, Exception e) {
        Map<String, String> context = buildContext(record);
        if (e != null) {
            context.put(EXCEPTION, e.getClass().getName());
        }
        withContext(context, () ->
            delegate.warn("[KAFKA-WARN] {} - error={}", message, e != null ? e.getMessage() : "null", e)
        );
    }

    public void log(String nivel, String mensagem, String classe, String metodo) {
        String logFormat = "[{}] {} - {}::{}";

        // Log no console
        switch (nivel.toUpperCase()) {
            case "DEBUG":
                if (delegate.isDebugEnabled()) delegate.debug(logFormat, nivel, mensagem, classe, metodo);
                break;
            case "INFO":
                delegate.info(logFormat, nivel, mensagem, classe, metodo);
                break;
            case "WARN":
                delegate.warn(logFormat, nivel, mensagem, classe, metodo);
                break;
            case "ERROR":
                delegate.error(logFormat, nivel, mensagem, classe, metodo);
                break;
            default:
                delegate.info("[UNDEFINED LEVEL] {} - {}::{}", mensagem, classe, metodo);
        }

        // Envio opcional para Kafka
        if (kafkaTemplateWrapper != null && kafkaTopic != null) {
            LogMessageDTO dto = new LogMessageDTO();
            dto.setLevel(nivel);
            dto.setMensagem(mensagem);
            dto.setClasse(classe);
            dto.setMetodo(metodo);
            dto.setTimestamp(System.currentTimeMillis());

            try {
                kafkaTemplateWrapper.send(kafkaTopic, dto);
            } catch (Exception e) {
                delegate.warn("Falha ao enviar log para Kafka: {}", e.getMessage(), e);
            }
        }
    }

    // Executa ação com MDC preenchido
    private void withContext(Map<String, String> context, Runnable action) {
        try {
            context.forEach(MDC::put);
            action.run();
        } finally {
            context.keySet().forEach(MDC::remove);
        }
    }

    // Gera contexto do MDC a partir de um ConsumerRecord
    private Map<String, String> buildContext(ConsumerRecord<?, ?> record) {
        Map<String, String> context = new HashMap<>();
        if (record != null) {
            context.put(TOPIC,     record.topic());
            context.put(PARTITION, String.valueOf(record.partition()));
            context.put(OFFSET,    String.valueOf(record.offset()));
            context.put(KEY,       String.valueOf(record.key()));
        }
        return context;
    }
}
