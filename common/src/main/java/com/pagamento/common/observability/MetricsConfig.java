package com.pagamento.common.observability;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
public class MetricsConfig {

    @Value("${spring.application.name:pagamento-service}")
    private String applicationName;
    
    // Lista de URIs a serem ignoradas
    private static final List<String> IGNORED_URIS = Arrays.asList(
        "/actuator", "/swagger", "/v3/api-docs", "/health"
    );

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags(
                "application", applicationName,
                "region", System.getenv().getOrDefault("AWS_REGION", "local"),
                "environment", System.getenv().getOrDefault("ENV", "dev")
            )
            .meterFilter(MeterFilter.deny(id -> shouldIgnoreUri(id)))
            .meterFilter(MeterFilter.maxExpected("http.server.requests", 10000L))
            .meterFilter(enableHistograms());
    }

    private boolean shouldIgnoreUri(Meter.Id id) {
        String uri = id.getTag("uri");
        if (uri == null) return false;
        
        return IGNORED_URIS.stream().anyMatch(uri::contains);
    }

    private MeterFilter enableHistograms() {
        return new MeterFilter() {
            @Override
            public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                if (id.getType() == Meter.Type.TIMER || id.getType() == Meter.Type.DISTRIBUTION_SUMMARY) {
                    return DistributionStatisticConfig.builder()
                        .percentiles(0.5, 0.75, 0.95, 0.99, 0.999)
                        .percentilesHistogram(true)
                        .expiry(Duration.ofMinutes(10))
                        .bufferLength(5)
                        .build()
                        .merge(config);
                }
                return config;
            }
        };
    }
}
