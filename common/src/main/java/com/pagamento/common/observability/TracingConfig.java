package com.pagamento.common.observability;

import io.opencensus.exporter.trace.jaeger.JaegerExporter;
import io.opencensus.exporter.trace.logging.LoggingTraceExporter;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.config.TraceConfig;
import io.opencensus.trace.samplers.Samplers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;

@Configuration
public class TracingConfig {

    @PostConstruct
    public void init() {
        // Configurar sampler (amostra 100% em dev, 10% em prod)
        String env = System.getenv().getOrDefault("ENV", "dev");
        TraceConfig traceConfig = Tracing.getTraceConfig();
        
        if ("prod".equals(env)) {
            traceConfig.updateActiveTraceParams(
                traceConfig.getActiveTraceParams()
                    .toBuilder()
                    .setSampler(Samplers.probabilitySampler(0.1))
                    .build()
            );
        } else {
            traceConfig.updateActiveTraceParams(
                traceConfig.getActiveTraceParams()
                    .toBuilder()
                    .setSampler(Samplers.alwaysSample())
                    .build()
            );
        }

        // Registrar exportadores
        LoggingTraceExporter.register();
        
        // Jaeger apenas se configurado
        if (System.getenv("JAEGER_ENDPOINT") != null) {
            JaegerExporter.createAndRegister(
                System.getenv("JAEGER_ENDPOINT"),
                "pagamento-service"
            );
        }
    }

    @Bean
    public CorrelationIdFilter correlationIdFilter() {
        return new CorrelationIdFilter();
    }
}
