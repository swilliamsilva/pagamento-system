package com.pagamento.common.observability;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;

// @Configuration
public class ObservabilityConfig implements WebMvcConfigurer {

    @Bean
    public ObservationFilter observationFilter(ObservationRegistry registry) {
        return new ObservationFilter(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(
            new ObservationHandlerInterceptor(
                observationRegistry(null) // Will be autowired by Spring
            )
        );
    }

    @Bean
    public ObservationRegistry observationRegistry(MeterRegistry meterRegistry) {
        ObservationRegistry registry = ObservationRegistry.create();
        
        if (meterRegistry != null) {
            registry.observationConfig()
                .observationHandler(
                    (ObservationHandler<Observation.Context>) 
                    new DefaultMeterObservationHandler(meterRegistry)
                );
        }
        
        // Optional: Add additional configuration
        registry.observationConfig()
            .observationPredicate((name, context) -> !name.startsWith("ignored"));
        
        return registry;
    }

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}