package com.pagamento.common.resilience;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class RetryConfig {

    @Value("${ENV:dev}")
    private String environment;

    private static final Set<Class<? extends Throwable>> RETRYABLE_EXCEPTIONS = new HashSet<>(Arrays.asList(
        java.net.ConnectException.class,
        java.net.SocketTimeoutException.class,
        org.springframework.web.client.ResourceAccessException.class,
        java.sql.SQLTransientException.class
    ));

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry.of(createDefaultConfig());
    }

    private RetryConfig createDefaultConfig() {
        boolean isProduction = "prod".equalsIgnoreCase(environment);
        int maxAttempts = isProduction ? 3 : 4;
        long initialInterval = isProduction ? 500 : 300;
        double multiplier = isProduction ? 1.5 : 2.0;

        return RetryConfig.custom()
            .maxAttempts(maxAttempts)
            .intervalFunction(RetryConfig.IntervalFunction.ofExponentialBackoff(
                Duration.ofMillis(initialInterval),
                multiplier
            ))
            .retryExceptions(RETRYABLE_EXCEPTIONS.toArray(new Class[0]))
            .failAfterMaxAttempts(true)
            .build();
    }

    @Bean(name = "externalServiceRetry")
    public Retry externalServiceRetry(RetryRegistry registry) {
        return registry.retry("externalService", RetryConfig.custom()
            .maxAttempts(5)
            .intervalFunction(RetryConfig.IntervalFunction.ofExponentialBackoff(
                Duration.ofSeconds(1),
                2.0
            ))
            .build());
    }


    @Bean(name = "databaseRetry")
    public Retry databaseRetry(RetryRegistry registry) {
        return registry.retry("database", RetryConfig.custom()
            .maxAttempts(4)
            .intervalFunction(RetryConfig.IntervalFunction.ofExponentialBackoff(
                Duration.ofMillis(200),
                1.5
            ))
            .build());
    }
}
