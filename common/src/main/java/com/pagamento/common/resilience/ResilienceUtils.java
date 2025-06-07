package com.pagamento.common.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ResilienceUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ResilienceUtils.class);

    public <T> T executeWithCircuitBreaker(CircuitBreaker circuitBreaker, Supplier<T> supplier) {
        return circuitBreaker.executeSupplier(supplier);
    }

    public <T> T executeWithRetry(Retry retry, Supplier<T> supplier) {
        return retry.executeSupplier(supplier);
    }

    public <T> T executeWithRateLimiter(RateLimiter rateLimiter, Supplier<T> supplier) {
        return rateLimiter.executeSupplier(supplier);
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
        Callable<T> callable = supplier::get;
        
        if (timeout != null) {
            TimeLimiter timeLimiter = TimeLimiter.of(timeout);
            callable = TimeLimiter.decorateFutureSupplier(
                timeLimiter, 
                () -> CompletableFuture.supplyAsync(supplier)
            );
        }
        
        if (circuitBreaker != null) {
            callable = CircuitBreaker.decorateCallable(circuitBreaker, callable);
        }
        
        if (retry != null) {
            callable = Retry.decorateCallable(retry, callable);
        }
        
        if (rateLimiter != null) {
            callable = RateLimiter.decorateCallable(rateLimiter, callable);
        }
        
        try {
            return callable.call();
        } catch (Exception e) {
            if (e.getCause() instanceof TimeoutException) {
                logger.error("Operation timed out after {}", timeout);
            }
            throw new ResilienceException("Execution failed after resilience mechanisms", e);
        }
    }
    
    public <T> T executeWithAllProtections(
        String operationName,
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
