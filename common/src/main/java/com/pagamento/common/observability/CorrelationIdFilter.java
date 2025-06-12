package com.pagamento.common.observability;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID = "X-Correlation-ID";
    private final Tracer tracer = GlobalOpenTelemetry.getTracer("payment-service");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String correlationId = request.getHeader(CORRELATION_ID);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        Span span = tracer.spanBuilder("HTTP " + request.getMethod()).startSpan();
        try (Scope scope = span.makeCurrent()) {
            MDC.put(CORRELATION_ID, correlationId);
            response.addHeader(CORRELATION_ID, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            span.end();
            MDC.remove(CORRELATION_ID);
        }
    }
} 
