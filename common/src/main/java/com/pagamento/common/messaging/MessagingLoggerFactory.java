/* ========================================================
# Classe: MessagingLoggerFactory
# Módulo: Messaging System - Logging Infrastructure
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, SLF4J 1.7, Maven - Junho de 2025
# ======================================================== */
package com.pagamento.common.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.Objects;

@Schema(description = "Fábrica de loggers para componentes de mensageria")
public final class MessagingLoggerFactory {

    private static final String LOGGER_PREFIX = "com.pagamento.messaging.";

    private MessagingLoggerFactory() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(LOGGER_PREFIX + clazz.getSimpleName());
    }

    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(LOGGER_PREFIX + name);
    }

    @Deprecated
    @Schema(deprecated = true, description = "Método obsoleto - usar getLogger(Class<?>)")
    public static Logger getLogger1(Class<?> clazz) {
        return getLogger(clazz);
    }

    @Deprecated
    @Schema(deprecated = true, description = "Método obsoleto - usar getLogger(Class<?>)")
    public static Logger getLogger11(Class<?> clazz) {
        return getLogger(clazz);
    }

    public static class ContextualLogger {
        private final Logger delegate;

        ContextualLogger(Logger delegate) {
            this.delegate = Objects.requireNonNull(delegate);
        }

        public void info(String message, Map<String, String> context) {
            try {
                context.forEach(MDC::put);
                delegate.info(message);
            } finally {
                context.keySet().forEach(MDC::remove);
            }
        }
    }
}