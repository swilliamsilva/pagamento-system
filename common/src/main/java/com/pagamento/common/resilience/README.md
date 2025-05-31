'''
Arquitetura de Resiliência com Resilience4j:

1. CircuitBreakerConfig:
   - Configuração global de Circuit Breaker
   - Configurações específicas para serviços críticos:
     • paymentServiceCircuitBreaker: Para serviços de pagamento
     • antiFraudServiceCircuitBreaker: Para serviço de antifraude
   - Parâmetros: taxa de falhas, tempo de espera, etc.

2. RetryConfig:
   - Configuração global de Retry
   - Configurações específicas para diferentes casos:
     • externalServiceRetry: Para chamadas externas
     • databaseRetry: Para operações de banco de dados
   - Parâmetros: tentativas, intervalo, exceções a tratar

3. RateLimiterConfig:
   - Configuração global de Rate Limiting
   - Configurações específicas:
     • paymentProcessingRateLimiter: Limitação para processamento de pagamentos
     • apiRateLimiter: Limitação global para APIs
   - Parâmetros: limite por período, tempo de refresh

4. ResilienceUtils:
   - Utilitário para simplificar o uso dos padrões
   - Métodos combinados para circuit breaker + retry + rate limiter
   - Suporte a fallbacks para tratamento de falhas

Como Usar:

1. Uso direto em serviços:

@Service
public class PaymentService {

    private final CircuitBreaker paymentCircuitBreaker;
    private final ResilienceUtils resilienceUtils;

    @Autowired
    public PaymentService(
        @Qualifier("paymentServiceCircuitBreaker") CircuitBreaker paymentCircuitBreaker,
        ResilienceUtils resilienceUtils
    ) {
        this.paymentCircuitBreaker = paymentCircuitBreaker;
        this.resilienceUtils = resilienceUtils;
    }

    public PaymentResult processPayment(PaymentRequest request) {
        return resilienceUtils.executeWithCircuitBreaker(
            paymentCircuitBreaker, 
            () -> gatewayService.process(request)
        );
    }
}

2. Uso combinado:

public PaymentResult processWithResilience(PaymentRequest request) {
    return resilienceUtils.executeWithResilience(
        () -> gatewayService.process(request),
        paymentCircuitBreaker, // Circuit breaker
        externalServiceRetry,  // Retry
        paymentRateLimiter     // Rate limiter
    );
}

3. Com fallback:

public PaymentResult processWithFallback(PaymentRequest request) {
    return resilienceUtils.executeWithFallback(
        () -> gatewayService.process(request),
        ex -> PaymentResult.error("Fallback due to error: " + ex.getMessage())
    );
}

Benefícios da Implementação:

1. Tolerância a Falhas:
   - Circuit Breaker: Evita sobrecarga em serviços instáveis
   - Retry: Recuperação automática de falhas transitórias
   - Fallback: Alternativas quando serviços estão indisponíveis

2. Proteção contra Sobrecarga:
   - Rate Limiting: Controle de fluxo para evitar sobrecarga
   - Limitação de requisições por segundo/minuto
   - Proteção contra ataques de negação de serviço

3. Resiliência Configurável:
   - Parâmetros ajustáveis por serviço
   - Configurações específicas para diferentes cenários
   - Fácil adaptação a mudanças de requisitos

4. Observabilidade:
   - Métricas expostas via Micrometer
   - Eventos registrados para monitoramento
   - Integração com Prometheus/Grafana

5. Padrões Combinados:
   - Uso integrado de circuit breaker + retry + rate limiter
   - Fallbacks para tratamento elegante de falhas
   - Timeouts configuráveis

Configurações Recomendadas:

- Circuit Breaker:
  • Produção: failureRateThreshold = 70-80%, waitDuration = 10-30s
  • Desenvolvimento: failureRateThreshold = 50%, waitDuration = 5s

- Retry:
  • Chamadas externas: 3-5 tentativas, intervalo exponencial
  • Banco de dados: 2-3 tentativas, intervalo fixo curto

- Rate Limiter:
  • APIs públicas: limite baseado em plano do cliente
  • Processamento interno: limite baseado em capacidade do sistema

Monitoramento:

As métricas do Resilience4j são expostas via Micrometer e podem ser monitoradas no Grafana:

1. Circuit Breaker:
   - Estado (aberto/fechado/meio aberto)
   - Taxa de falhas
   - Número de chamadas rejeitadas

2. Retry:
   - Tentativas bem sucedidas com retry
   - Tentativas falhas após retry
   - Número médio de tentativas

3. Rate Limiter:
   - Número de chamadas permitidas
   - Chamadas rejeitadas
   - Tempo de espera

Esta implementação transforma o sistema de pagamentos em uma aplicação altamente resiliente, capaz de lidar com falhas de infraestrutura, sobrecarga de serviços e condições adversas de rede.
'''
# passo_17_impl_resilience4j.py
'''
As classes serão geradas na estrutura:

    common/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── pagamento/
                        └── common/
                            └── resilience/
                                ├── CircuitBreakerConfig.java
                                ├── RetryConfig.java
                                ├── RateLimiterConfig.java
                                └── ResilienceUtils.java

Componentes principais:

1. CircuitBreakerConfig.java
    Configuração global de circuit breakers
    Instâncias específicas para serviços críticos:
        paymentServiceCircuitBreaker: Para serviços de pagamento
        antiFraudServiceCircuitBreaker: Para serviço de antifraude
    Parâmetros configuráveis: taxa de falhas, tempo de espera, etc.

2. RetryConfig.java

    Configuração global de políticas de retry
    Configurações específicas:
        externalServiceRetry: Para chamadas a serviços externos
        databaseRetry: Para operações de banco de dados
    Controle de tentativas e intervalos

3. RateLimiterConfig.java

    Configuração global de rate limiters
    Configurações específicas:
        paymentProcessingRateLimiter: Para processamento de pagamentos
        apiRateLimiter: Para limitação global de API
    Limites por período e tempo de refresh

4. ResilienceUtils.java

    Utilitário para simplificar o uso dos padrões
    Métodos combinados para circuit breaker + retry + rate limiter
    Suporte a fallbacks para tratamento elegante de falhas

Configurações Padrão:
Mecanismo	Parâmetro	Valor Padrão	Descrição
Circuit Breaker	failureRateThreshold	50%	Taxa de falhas para abrir circuito
	slowCallDurationThreshold	2 segundos	Limite para chamadas lentas
	waitDurationInOpenState	30 segundos	Tempo em estado aberto
Retry	maxAttempts	3	Número máximo de tentativas
	waitDuration	500 ms	Intervalo entre tentativas
Rate Limiter	limitForPeriod	100	Chamadas permitidas por período
	limitRefreshPeriod	1 segundo	Período para recarregar limite

Benefícios da Implementação:

    Tolerância a Falhas: Recuperação automática de erros transitórios
    Prevenção de Cascatas: Circuit breakers isolam serviços problemáticos
    Controle de Fluxo: Rate limiters previnem sobrecarga do sistema
    Respostas Elegantes: Fallbacks fornecem alternativas quando serviços falham
    Auto-Cura: Sistemas se recuperam automaticamente após falhas
    Monitoramento: Métricas detalhadas para análise de resiliência

Esta implementação transforma o sistema de pagamentos em uma aplicação altamente resiliente, capaz de lidar com falhas de infraestrutura, sobrecarga de serviços e condições adversas de rede, garantindo disponibilidade e confiabilidade mesmo em cenários desfavoráveis.


'''
