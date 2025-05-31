package com.pagamento.common.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ObservabilityUtils {

    private final MeterRegistry meterRegistry;
    private final Tracer tracer;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public ObservabilityUtils(MeterRegistry meterRegistry, Tracer tracer) {
        this.meterRegistry = meterRegistry;
        this.tracer = tracer;
    }

    public Counter getOrCreateCounter(String name, String description, String... tags) {
        String key = name + String.join(",", tags);
        return counters.computeIfAbsent(key, k -> 
            Counter.builder(name)
                .description(description)
                .tags(tags)
                .register(meterRegistry)
        );
    }

    public Timer getOrCreateTimer(String name, String description, String... tags) {
        String key = name + String.join(",", tags);
        return timers.computeIfAbsent(key, k -> 
            Timer.builder(name)
                .description(description)
                .tags(tags)
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry)
        );
    }

    public Span startSpan(String name) {
        if (tracer == null) return null;
        return tracer.nextSpan().name(name).start();
    }

    public void endSpan(Span span) {
        if (span != null) {
            span.end();
        }
    }

    public void recordTimer(Timer timer, long startTime) {
        if (timer != null) {
            timer.record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
        }
    }

    public void recordException(Throwable exception, String errorType, String operation) {
        if (exception == null) return;
        
        getOrCreateCounter("app_errors", "Application errors count", 
            "error_type", errorType, 
            "operation", operation,
            "exception", exception.getClass().getSimpleName()
        ).increment();
    }

    public void logEvent(String eventName, Map<String, String> context) {
        if (tracer == null) return;
        
        Span span = tracer.currentSpan();
        if (span != null) {
            if (context != null) {
                context.forEach(span::tag);
            }
            span.event(eventName);
        }
    }

    public void recordBusinessMetric(String name, double value, String... tags) {
        if (meterRegistry != null) {
            AtomicReference<Double> gauge = new AtomicReference<>(value);
            meterRegistry.gauge(name, Tags.of(tags), gauge, AtomicReference::get);
        }
    }
}
