package com.pagamento.common.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name:pagamento-service}")
    private String applicationName;

    @Bean
    public MeterRegistry meterRegistry() {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
        
        // Configurações comuns
        registry.config()
            .commonTags(
                "application", applicationName,
                "region", System.getenv().getOrDefault("AWS_REGION", "local"),
                "environment", System.getenv().getOrDefault("ENV", "dev")
            )
            .meterFilter(MeterFilter.deny(id -> {
                String uri = id.getTag("uri");
                return uri != null && uri.contains("actuator");
            }))
            .meterFilter(new MeterFilter() {
                @Override
                public DistributionStatisticConfig configure(
                    Meter.Id id, 
                    DistributionStatisticConfig config
                ) {
                    // Habilita histogramas para timers
                    if (id.getType() == Meter.Type.TIMER) {
                        return DistributionStatisticConfig.builder()
                            .percentiles(0.5, 0.75, 0.95, 0.99)
                            .percentilesHistogram(true)
                            .expiry(Duration.ofMinutes(5))
                            .build()
                            .merge(config);
                    }
                    return config;
                }
            });

        // Registrar métricas padrão
        new JvmMemoryMetrics().bindTo(registry);
        new JvmGcMetrics().bindTo(registry);
        new JvmThreadMetrics().bindTo(registry);
        new ProcessorMetrics().bindTo(registry);
        new UptimeMetrics().bindTo(registry);
        new ClassLoaderMetrics().bindTo(registry);

        return registry;
    }

    @Bean
    public MeterFilter ignoreStaticResourcesFilter() {
        return MeterFilter.deny(id -> {
            String uri = id.getTag("uri");
            return uri != null && 
                (uri.startsWith("/static") || 
                 uri.startsWith("/actuator") || 
                 uri.startsWith("/swagger") ||
                 uri.startsWith("/v3/api-docs"));
        });
    }
}
