package com.pagamento.common.resilience;

import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

/**
 * The import io.github.resilience4j.retry.RetryConfig conflicts with a type defined in the same file
 * 
 * */

import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
/*
 * Configuration is not an annotation type
 * **/
public class RetryConfig {

    @Value("${ENV:dev}")
    private String environment;

    @SuppressWarnings("unchecked")
    private static final Set<Class<? extends Throwable>> RETRYABLE_EXCEPTIONS = new HashSet<Class<? extends Throwable>>(
       /*
        * he constructor HashSet<Class<? extends Throwable>>(Arrays.asList(java.net.ConnectException.class, java.net.SocketTimeoutException.class, 
 org.springframework.web.client.ResourceAccessException.class, java.sql.SQLTransientException.class)) is undefined
 
        * **/
    		
    		Arrays.asList(
            java.net.ConnectException.class,
            java.net.SocketTimeoutException.class,
            org.springframework.web.client.ResourceAccessException.class,
            java.sql.SQLTransientException.class
        )
    );

    @Bean
    public RetryRegistry retryRegistry() {
        return RetryRegistry.ofDefaults();
    }

    private io.github.resilience4j.retry.RetryConfig createDefaultConfig() {
        boolean isProduction = "prod".equalsIgnoreCase(environment);
        
        return io.github.resilience4j.retry.RetryConfig.custom()
            .maxAttempts(isProduction ? 3 : 4)
            .intervalFunction(IntervalFunction.ofExponentialBackoff(
                Duration.ofMillis(isProduction ? 500 : 300),
                isProduction ? 1.5 : 2.0
            ))
            .retryOnException(e -> RETRYABLE_EXCEPTIONS.stream()
                .anyMatch(clazz -> clazz.isInstance(e)))
            .failAfterMaxAttempts(false)
            .build();
    }

    @Bean(name = "externalServiceRetry")
    public Retry externalServiceRetry(RetryRegistry registry) {
        return registry.retry("externalService", createDefaultConfig());
    }

    @Bean(name = "databaseRetry")
    public Retry databaseRetry(RetryRegistry registry) {
        return registry.retry("database", 
            io.github.resilience4j.retry.RetryConfig.custom()
                .maxAttempts(4)
                .intervalFunction(IntervalFunction.ofExponentialBackoff(
                    Duration.ofMillis(200),
                    1.5
                ))
                .retryOnException(e -> RETRYABLE_EXCEPTIONS.stream()
                    .anyMatch(clazz -> clazz.isInstance(e)))
                .failAfterMaxAttempts(false)
                .build()
        );
    }
}