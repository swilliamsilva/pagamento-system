package com.pagamento.common.resilience;

import io.github.resilience4j.ratelimiter.RateLimiter;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class RateLimiterConfig {

    @Value("${ENV:dev}")
    private String environment;

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.of(createDefaultConfig());
    }

    private RateLimiterConfig createDefaultConfig() {
        boolean isProduction = "prod".equalsIgnoreCase(environment);

        return RateLimiterConfig.custom()
            .limitForPeriod(isProduction ? 200 : 100)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMillis(isProduction ? 100 : 500))
            .build();
    }

    @Bean(name = "paymentProcessingRateLimiter")
    public RateLimiter paymentProcessingRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("paymentProcessing", RateLimiterConfig.custom()
            .limitForPeriod(50)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMillis(300))
            .build());
    }

    @Bean(name = "apiRateLimiter")
    public RateLimiter apiRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("api", RateLimiterConfig.custom()
            .limitForPeriod(1000)
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .build());
    }
}
