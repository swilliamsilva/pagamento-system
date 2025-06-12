package com.pagamento.common.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class TracingConfig {

    @Value("${spring.application.name:payment-service}")
    private String applicationName;

    @Value("${otel.exporter.otlp.endpoint:http://localhost:4317}")
    private String otlpEndpoint;

    @Value("${ENV:dev}")
    private String environment;

    @Bean
    public OpenTelemetrySdk openTelemetrySdk() {
        Resource resource = Resource.getDefault()
            .merge(Resource.builder()
                .put(io.opentelemetry.api.common.AttributeKey.stringKey("service.name"), applicationName)
                .build());

        Sampler sampler = "prod".equalsIgnoreCase(environment)
            ? Sampler.traceIdRatioBased(0.1)
            : Sampler.alwaysOn();

        SpanExporter exporter = OtlpGrpcSpanExporter.builder()
            .setEndpoint(otlpEndpoint)
            .setTimeout(2, TimeUnit.SECONDS)
            .build();

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(BatchSpanProcessor.builder(exporter).build())
            .setSampler(sampler)
            .setResource(resource)
            .build();

        OpenTelemetrySdk sdk = OpenTelemetrySdk.builder()
            .setTracerProvider(tracerProvider)
            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
            .build();

        GlobalOpenTelemetry.set(sdk);
        return sdk;
    }

    @Bean
    public Tracer otelTracer(OpenTelemetrySdk sdk) {
        return sdk.getTracerProvider().get("payment-service");
    }
} 
