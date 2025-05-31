package com.pagamento.common.observability;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ServerHttpObservationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ObservabilityConfig implements WebMvcConfigurer {

    @Bean
    public ServerHttpObservationFilter observationFilter() {
        return new ServerHttpObservationFilter();
    }
}
