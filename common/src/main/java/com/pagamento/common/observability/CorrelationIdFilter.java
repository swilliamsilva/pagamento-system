package com.pagamento.common.observability;

import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.propagation.TextFormat;

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
    /*
     * Type mismatch: cannot convert from com.pagamento.common.observability.TextFormat to io.opencensus.trace.propagation.TextFormat
     * */
    private static final TextFormat textFormat = TraceContextFormat.getInstance();
    
    /*
     * Type mismatch: cannot convert from com.pagamento.common.observability.TextFormat to io.opencensus.trace.propagation.TextFormat
     * */
    private static final Tracer tracer = Tracing.getTracer();

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

        TextFormat.Getter<HttpServletRequest> getter = new TextFormat.Getter<HttpServletRequest>() {
            @Override
            public String get(HttpServletRequest carrier, String key) {
                return carrier.getHeader(key);
            }
        };

        Span span = tracer.spanBuilderWithRemoteParent("HTTP " + request.getMethod(), textFormat.extract(request, getter))
                .startSpan();

        try (io.opencensus.common.Scope scope = tracer.withSpan(span)) {
            MDC.put(CORRELATION_ID, correlationId);
            response.addHeader(CORRELATION_ID, correlationId);
            filterChain.doFilter(request, response);
        } finally {
            span.end();
            MDC.remove(CORRELATION_ID);
        }
    }
}
