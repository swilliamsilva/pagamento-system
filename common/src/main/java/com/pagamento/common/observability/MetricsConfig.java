package com.pagamento.common.observability;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class MetricsConfig {

    @Value("${spring.application.name:pagamento-service}")
    private String applicationName;
    
    private static final List<String> IGNORED_URIS = Arrays.asList(
        new String[]{"/actuator", "/swagger", "/v3/api-docs", "/health"}
    );

    @Bean
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags(
        @Value("${management.metrics.tags.application:${spring.application.name:application}}") String appName) {
        
        return registry -> {
            registry.config()
                .commonTags(
                    "application", appName,
                    "region", System.getenv().getOrDefault("AWS_REGION", "local"),
                    "environment", System.getenv().getOrDefault("ENV", "dev")
                )
                .meterFilter(MeterFilter.deny(this::shouldIgnoreUri))
                .meterFilter(enableHistograms());
        };
    }
    
    private boolean shouldIgnoreUri(Meter.Id id) {
        String uri = id.getTag("uri");
        return uri != null && IGNORED_URIS.stream().anyMatch(uri::contains);
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