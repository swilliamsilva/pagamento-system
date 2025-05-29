package com.pagamento.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("auth_route", r -> r.path("/auth/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://auth-service"))
                
            .route("payment_route", r -> r.path("/payment/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://payment-service"))
                
            .route("boleto_route", r -> r.path("/boleto/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://boleto-service"))
                
            .route("pix_route", r -> r.path("/pix/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://pix-service"))
                
            .route("card_route", r -> r.path("/card/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://card-service"))
                
            .route("pagbank_route", r -> r.path("/pagbank/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://pagbank-integration"))
                
            .route("itau_route", r -> r.path("/itau/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://itau-integration"))
                
            .route("santander_route", r -> r.path("/santander/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://santander-integration"))
                
            .route("caixa_route", r -> r.path("/caixa/**")
                .filters(f -> f.stripPrefix(1))
                .uri("lb://caixa-integration"))
            .build();
    }
}
