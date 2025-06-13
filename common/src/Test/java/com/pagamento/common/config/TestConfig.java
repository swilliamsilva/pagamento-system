package com.pagamento.common.config;

import io.micrometer.tracing.Tracer;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Tracer tracer() {
        return Mockito.mock(Tracer.class);
    }
}
