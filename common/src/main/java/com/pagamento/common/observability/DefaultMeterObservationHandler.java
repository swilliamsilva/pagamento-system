package com.pagamento.common.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Tags;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DefaultMeterObservationHandler implements ObservationHandler<Observation.Context> {

    private static final String START_TIME_KEY = "startTime"; // Chave para armazenar o tempo de início

    private final MeterRegistry meterRegistry;

    public DefaultMeterObservationHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void onStart(Observation.Context context) {
        // Armazena o tempo de início no contexto
        context.put(START_TIME_KEY, System.nanoTime());

        Counter.builder("observation.start")
            .tags(Tags.of(
                "name", context.getName(),
                "context", context.getClass().getSimpleName()
            ))
            .register(meterRegistry)
            .increment();
    }

    @Override
    public void onError(Observation.Context context) {
        if (context.getError() != null) {
            Counter.builder("observation.error")
                .tags(Tags.of(
                    "name", context.getName(),
                    "error", context.getError().getClass().getSimpleName()
                ))
                .register(meterRegistry)
                .increment();
        }
    }

    @Override
    public void onEvent(Observation.Event event, Observation.Context context) {
        Counter.builder("observation.event")
            .tags(Tags.of(
                "name", context.getName(),
                "event", event.getName()
            ))
            .register(meterRegistry)
            .increment();
    }

    @Override
    public void onStop(Observation.Context context) {
        // Recupera o tempo de início armazenado no contexto
        Long startTime = (Long) context.get(START_TIME_KEY); // Cast explícito
        if (startTime == null) {
            startTime = 0L; // Fallback se não existir
        }
        
        long durationNanos = System.nanoTime() - startTime;

        Timer.builder("observation.duration")
            .tags(Tags.of(
                "name", context.getName(),
                "status", context.getError() != null ? "error" : "success"
            ))
            .register(meterRegistry)
            .record(durationNanos, TimeUnit.NANOSECONDS);
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}