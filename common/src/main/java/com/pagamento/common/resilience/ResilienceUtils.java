package com.pagamento.common.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ResilienceUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ResilienceUtils.class);

    public <T> T executeWithCircuitBreaker(CircuitBreaker circuitBreaker, Supplier<T> supplier) {
        return CircuitBreaker.decorateSupplier(circuitBreaker, supplier).get();
    }

    public <T> T executeWithRetry(Retry retry, Supplier<T> supplier) {
        return Retry.decorateSupplier(retry, supplier).get();
    }

    public <T> T executeWithRateLimiter(RateLimiter rateLimiter, Supplier<T> supplier) {
        return RateLimiter.decorateSupplier(rateLimiter, supplier).get();
    }

    public <T> T executeWithFallback(Supplier<T> supplier, Function<Throwable, T> fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            logger.warn("Fallback triggered due to exception: {}", e.getMessage());
            return fallback.apply(e);
        }
    }

    public <T> T executeWithResilience(
        Supplier<T> supplier, 
        CircuitBreaker circuitBreaker, 
        Retry retry, 
        RateLimiter rateLimiter,
        Duration timeout
    ) {
        Supplier<T> decoratedSupplier = supplier;
        
        if (circuitBreaker != null) {
            decoratedSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, decoratedSupplier);
        }
        
        if (retry != null) {
            decoratedSupplier = Retry.decorateSupplier(retry, decoratedSupplier);
        }
        
        if (rateLimiter != null) {
            decoratedSupplier = RateLimiter.decorateSupplier(rateLimiter, decoratedSupplier);
        }
        
        if (timeout != null) {
            TimeLimiter timeLimiter = TimeLimiter.of(TimeLimiterConfig.custom()
                .timeoutDuration(timeout)
                .build());

            // Capture the current decoratedSupplier in a final variable
            final Supplier<T> finalSupplier = decoratedSupplier;
            decoratedSupplier = () -> {
                try {
                    return timeLimiter.executeFutureSupplier(() -> 
                        CompletableFuture.supplyAsync(finalSupplier)
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
        }
        
        try {
            return decoratedSupplier.get();
        } catch (Exception e) {
            if (e.getCause() instanceof TimeoutException) {
                logger.error("Operation timed out after {}", timeout);
                throw new RuntimeException("Operation timed out after " + timeout, e);
            }
            throw new RuntimeException("Execution failed after resilience mechanisms", e);
        }
    }
    
    public <T> T executeWithAllProtections(
        Supplier<T> supplier,
        CircuitBreaker circuitBreaker,
        Retry retry,
        RateLimiter rateLimiter,
        Duration timeout,
        Function<Throwable, T> fallback
    ) {
        Supplier<T> decoratedSupplier = () -> 
            executeWithResilience(supplier, circuitBreaker, retry, rateLimiter, timeout);
        
        return executeWithFallback(decoratedSupplier, fallback);
    }
}
