package com.pagamento.common.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import com.pagamento.common.resilience.exception.ResilienceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utilitário para execução resiliente de operações
 * 
 * <p>Fornece métodos para aplicar padrões de resiliência como:</p>
 * <ul>
 *   <li>Circuit Breaker</li>
 *   <li>Retry</li>
 *   <li>Rate Limiting</li>
 *   <li>Timeouts</li>
 *   <li>Fallbacks</li>
 * </ul>
 */
@Component
public class ResilienceUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(ResilienceUtils.class);

    /**
     * Executa uma operação com Circuit Breaker
     */
    public <T> T executeWithCircuitBreaker(CircuitBreaker circuitBreaker, Supplier<T> supplier) {
        return circuitBreaker.executeSupplier(supplier);
    }

    /**
     * Executa uma operação com política de Retry
     */
    public <T> T executeWithRetry(Retry retry, Supplier<T> supplier) {
        return retry.executeSupplier(supplier);
    }

    /**
     * Executa uma operação com Rate Limiter
     */
    public <T> T executeWithRateLimiter(RateLimiter rateLimiter, Supplier<T> supplier) {
        return rateLimiter.executeSupplier(supplier);
    }

    /**
     * Executa uma operação com fallback
     */
    public <T> T executeWithFallback(Supplier<T> supplier, Function<Throwable, T> fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            logger.warn("Fallback triggered due to exception: {}", e.getMessage());
            return fallback.apply(e);
        }
    }

    /**
     * Executa uma operação com múltiplos mecanismos de resiliência
     */
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
                throw new ResilienceException("Operation timed out after " + timeout, e);
            }
            throw new ResilienceException("Execution failed after resilience mechanisms", e);
        }
    }
    
    /**
     * Executa uma operação com todos os mecanismos de proteção e fallback
     */
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