package com.pagamento.common.observability;

import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.TextMapPropagator;

public class TraceContextFormat {

    private static final TextMapPropagator INSTANCE = W3CTraceContextPropagator.getInstance();

    public static TextMapPropagator getInstance() {
        return INSTANCE;
    }
} 
