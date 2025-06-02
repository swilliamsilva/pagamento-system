package com.pagamento.common.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class CircuitBreakerConfig {

    @Value("${ENV:dev}")
    private String environment;

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(MeterRegistry meterRegistry) {
        CircuitBreakerConfig defaultConfig = createDefaultConfig();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(defaultConfig);
        
        // Registrar métricas para todos os circuit breakers
        registry.getEventPublisher().onEntryAdded(event -> 
            event.getAddedEntry().getEventPublisher().onStateTransition(e -> 
                meterRegistry.counter("resilience4j.circuitbreaker.state.transitions",
                    "name", e.getCircuitBreakerName(),
                    "state", e.getStateTransition().getToState().name())
                .increment()
            )
        );
        
        return registry;
    }

    private CircuitBreakerConfig createDefaultConfig() {
        boolean isProduction = "prod".equalsIgnoreCase(environment);
        
        return CircuitBreakerConfig.custom()
            .failureRateThreshold(isProduction ? 40 : 60) 
            .slowCallRateThreshold(isProduction ? 30 : 50)
            .slowCallDurationThreshold(Duration.ofMillis(isProduction ? 1000 : 2000))
            .waitDurationInOpenState(Duration.ofSeconds(isProduction ? 60 : 30))
            .permittedNumberOfCallsInHalfOpenState(isProduction ? 10 : 5)
            .minimumNumberOfCalls(isProduction ? 100 : 20)
            .slidingWindowType(SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(isProduction ? 100 : 50)
            .recordExceptions(Exception.class)
            .ignoreExceptions(IllegalArgumentException.class)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .writableStackTraceEnabled(false) // Melhoria de performance
            .build();
    }

    private static Object custom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Bean(name = "paymentServiceCircuitBreaker")
    public CircuitBreaker paymentServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("paymentService", CircuitBreakerConfig.custom()
            .failureRateThreshold(30)
            .slowCallDurationThreshold(Duration.ofMillis(800))
            .build());
    }

    @Bean(name = "antiFraudServiceCircuitBreaker")
    public CircuitBreaker antiFraudServiceCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("antiFraudService", CircuitBreakerConfig.custom()
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofMinutes(2))
            .build());
    }

    @Bean(name = "externalApiCircuitBreaker")
    public CircuitBreaker externalApiCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("externalApi", CircuitBreakerConfig.custom()
            .failureRateThreshold(60)
            .waitDurationInOpenState(Duration.ofMinutes(5))
            .build());
    }
}
