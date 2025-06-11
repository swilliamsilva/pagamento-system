/* ========================================================
# DOCUMENTAÇÃO OPENAPI - CONFIGURAÇÃO DE RATE LIMITER
# 
# Especificação para configuração de limite de requisições
# Classe: RateLimiterConfig
# Módulo: pagamento-common-resilience
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# ======================================================== */
package com.pagamento.common.resilience;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterConfig; // Import correto
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

@Configuration
public class AppRateLimiterConfig { // Renomeei a classe para evitar conflito

    @Value("${ENV:dev}")
    private String environment;

    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        return RateLimiterRegistry.of(createDefaultConfig());
    }

    private RateLimiterConfig createDefaultConfig() {
        boolean isProduction = "prod".equalsIgnoreCase(environment);

        return RateLimiterConfig.custom() // Usando a classe do Resilience4j
            .limitForPeriod(isProduction ? 200 : 100)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMillis(isProduction ? 100 : 500))
            .build();
    }

    @Bean(name = "paymentProcessingRateLimiter")
    public RateLimiter paymentProcessingRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("paymentProcessing", RateLimiterConfig.custom()
            .limitForPeriod(50)
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofMillis(300))
            .build());
    }

    @Bean(name = "apiRateLimiter")
    public RateLimiter apiRateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("api", RateLimiterConfig.custom()
            .limitForPeriod(1000)
            .limitRefreshPeriod(Duration.ofMinutes(1))
            .build());
    }
}


/*

Fluxo de Processamento

Estágio	Descrição

Entrada	Requisição HTTP recebida por um endpoint protegido por 
Rate Limiter
Processo
1. Verifica se a requisição está dentro do limite 
configurado
2. Contabiliza a requisição
3. Decide permitir ou bloquear

Saída	
- Requisição processada (200)
- Limite excedido (429)

Relacionamentos com Outras Classes

Classe	Tipo de Relação	Descrição
RateLimiterRegistry	Composição	Registro central de limitadores de taxa
RateLimiter	Produção	Instâncias de limitadores criadas pela classe
@RestController	Uso	Classes que utilizam os rate limiters configurados
Configurações de Rate Limiting
Limiter Name	Requests/Sec	Timeout	Ambiente
Padrão (DEV)	100	500ms	Dev
Padrão (PROD)	200	100ms	Prod
paymentProcessing	50	300ms	Todos
api	1000/min	-	Todos  

**/