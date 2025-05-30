package com.pagamento.common.observability;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ObservabilityUtils {

    private final MeterRegistry meterRegistry;
    private final Tracer tracer;

    public ObservabilityUtils(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.tracer = Tracing.getTracer();
    }

    public Counter createCounter(String name, String description, String... tags) {
        return Counter.builder(name)
            .description(description)
            .tags(tags)
            .register(meterRegistry);
    }

    public Timer createTimer(String name, String description, String... tags) {
        return Timer.builder(name)
            .description(description)
            .tags(tags)
            .register(meterRegistry);
    }

    public Span startSpan(String name) {
        return tracer.spanBuilder(name).startSpan();
    }

    public void recordTimer(Timer timer, long startTime) {
        timer.record(System.currentTimeMillis() - startTime, TimeUnit.MILLISECONDS);
    }

    public void recordException(Throwable exception, Map<String, String> tags) {
        Counter counter = Counter.builder("app_errors")
            .description("Application errors count")
            .tags(tags)
            .tag("exception", exception.getClass().getSimpleName())
            .register(meterRegistry);
        counter.increment();
    }

    public void logEvent(Span span, String eventName, Map<String, String> attributes) {
        if (span != null) {
            span.addAnnotation(eventName, attributes);
        }
    }
}
