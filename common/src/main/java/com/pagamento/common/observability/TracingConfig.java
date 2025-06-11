/* ========================================================
# Classe: TracingConfig
# Módulo: pagamento-common-observability
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# ======================================================== */

package com.pagamento.common.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.otel.bridge.*;
import io.opentelemetry.api.GlobalOpenTelemetry;
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

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME;

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
                .put(SERVICE_NAME, applicationName)
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
    public Tracer otelTracer(OpenTelemetrySdk sdk, MeterRegistry meterRegistry) {
        // Configuração simplificada para Spring Boot 2.7
        io.opentelemetry.api.trace.Tracer otelTracer = sdk.getTracerProvider().get("payment-service");
        
        return new OtelTracer(
            otelTracer,
            new OtelCurrentTraceContext(),
            event -> {},
            new OtelBaggageManager(new OtelCurrentTraceContext(), Collections.emptyList(), Collections.emptyList()),
            Collections.emptyList(),
            Collections.emptyList(),
            meterRegistry
        );
    }

    @Bean
    public OtelPropagator propagator() {
        return new OtelPropagator(new ArrayList<>(), new ArrayList<>());
    }
}

/**
 * @Configuration para configuração de tracing distribuído
 * 
 * <p>Configura a integração entre Micrometer Tracing e OpenTelemetry
 * para coleta e exportação de traces em formato OTLP.</p>
 * 
 * <h2>Fluxo Principal</h2>
 * <ol>
 *   <li><b>Entrada:</b> 
 *     <ul>
 *       <li>Nome da aplicação (spring.application.name)</li>
 *       <li>Endpoint do coletor OTLP (otel.exporter.otlp.endpoint)</li>
 *       <li>Ambiente (ENV)</li>
 *     </ul>
 *   </li>
 *   <li><b>Processamento:</b>
 *     <ul>
 *       <li>Criação do recurso OpenTelemetry com nome do serviço</li>
 *       <li>Configuração do sampler baseado no ambiente</li>
 *       <li>Criação do exportador OTLP gRPC</li>
 *       <li>Configuração do provedor de traces</li>
 *       <li>Integração com Micrometer Tracing</li>
 *     </ul>
 *   </li>
 *   <li><b>Saída:</b>
 *     <ul>
 *       <li>SDK OpenTelemetry configurado</li>
 *       <li>Tracer Micrometer pronto para uso</li>
 *       <li>Propagador de contexto configurado</li>
 *     </ul>
 *   </li>
 * </ol>
 * 
 * <h2>Relação com Outras Classes</h2>
 * <table>
 *   <tr><th>Classe</th><th>Relação</th></tr>
 *   <tr><td>OpenTelemetrySdk</td><td>SDK principal do OpenTelemetry</td></tr>
 *   <tr><td>Tracer</td><td>Interface do Micrometer para tracing</td></tr>
 *   <tr><td>Propagator</td><td>Propagação de contexto entre serviços</td></tr>
 *   <tr><td>OtlpGrpcSpanExporter</td><td>Exporta traces para coletor OTLP</td></tr>
 * </table>
 */

    /**
     * Configura o SDK OpenTelemetry
     * 
     * @return SDK configurado com exportador OTLP
     */

    /**
     * Cria o Tracer do Micrometer baseado no OpenTelemetry
     * 
     * @param sdk SDK OpenTelemetry
     * @return Tracer configurado
     */

    /**
     * Configura o propagador de contexto
     * 
     * @return Propagador baseado no W3C Trace Context
     */
