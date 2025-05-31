# Arquitetura de Observabilidade

## 1. Componentes Principais

### TracingConfig
- Configura o tracing distribuído utilizando **Micrometer Tracing**.
- Integração com **OpenTelemetry**, adotando o padrão **W3C Trace Context**.
- Suporte à propagação de contexto entre serviços.
- Registro automático de observações com handlers para **tracing** e **logging**.

### MetricsConfig
- Configuração centralizada de métricas via **Micrometer**.
- Inclusão de tags comuns para identificação (ex: `app`, `região`, `ambiente`).
- Filtros para exclusão de endpoints irrelevantes (ex: actuator, swagger).
- Histogramas configurados para timers.
- Renomeação de tags para padronização.

### ObservabilityUtils
- Utilitário para facilitar a instrumentação de código:
  - Criação de contadores e timers.
  - Gerenciamento de spans.
  - Registro de eventos e exceções.
  - Medição de tempo de execução.

## 2. Dependências (pom.xml)

```xml
<properties>
    <micrometer.version>1.12.0</micrometer.version>
    <micrometer-tracing.version>1.2.0</micrometer-tracing.version>
    <opentelemetry.version>1.35.0</opentelemetry.version>
</properties>

<dependencies>
    <!-- Observabilidade com Micrometer e OpenTelemetry -->
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-core</artifactId>
        <version>${micrometer.version}</version>
    </dependency>
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-tracing</artifactId>
        <version>${micrometer-tracing.version}</version>
    </dependency>
    <dependency>
        <groupId>io.micrometer</groupId>
        <artifactId>micrometer-tracing-bridge-otel</artifactId>
        <version>${micrometer-tracing.version}</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-api</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-sdk</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-exporter-otlp</artifactId>
        <version>${opentelemetry.version}</version>
    </dependency>
</dependencies>
```

## 3. Como Utilizar

### Tracing
- Anotar métodos com `@Observed` para tracing automático.
- Utilizar `ObservabilityUtils` para criação manual de spans.

### Métricas
- Criar contadores e timers com `ObservabilityUtils`.
- Registrar exceções com `recordException()`.

## 4. Exemplo de Instrumentação

```java
@PostMapping("/payment")
public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
    long start = System.currentTimeMillis();
    Span span = observabilityUtils.startSpan("process-payment");

    try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
        observabilityUtils.logEvent("payment.started", Map.of(
            "amount", request.getAmount().toString(),
            "currency", request.getCurrency()
        ));

        PaymentResponse response = paymentService.process(request);

        observabilityUtils.logEvent("payment.completed", Map.of(
            "transactionId", response.getTransactionId(),
            "status", response.getStatus()
        ));

        return response;
    } catch (Exception e) {
        observabilityUtils.recordException(e, Map.of(
            "operation", "payment.process",
            "errorType", "processing_error"
        ));
        throw e;
    } finally {
        span.end();
        observabilityUtils.recordTimer(paymentTimer, start);
    }
}
```

## 5. Benefícios

### Visibilidade Completa
- Traces distribuídos entre serviços.
- Métricas de desempenho e erros.
- Correlação entre logs, traces e métricas.

### Padrões Abertos
- Suporte completo ao **OpenTelemetry**.
- Compatível com backends como **Prometheus**, **Jaeger**, **Zipkin**, entre outros.

### Desempenho
- Coleta eficiente e com baixo overhead.
- Amostragem configurável conforme ambiente.

### Integração
- Compatível com **Spring Boot Actuator**.
- Exportação nativa para diversos sistemas e cloud providers.

### Operacionalização
- Dashboards prontos.
- Alertas baseados em **SLOs**.
- Suporte à detecção de anomalias.

## 6. Suporte a Java 8 (Alternativa com OpenCensus)

### Principais Ajustes
- Substituição de **Micrometer Tracing** por **OpenCensus**.
- Utilização de bibliotecas compatíveis com Java 8 (ex: `opencensus 0.31.1`).
- Redução de dependências incompatíveis.

### Tracing com OpenCensus
- Exportação de spans para **Jaeger**.
- Amostragem adaptativa (ambientes dev/prod).
- Filtro de Correlation ID para logs.

### Métricas com Micrometer
- Integração com **Prometheus**.
- Coleta automática de métricas da JVM.
- Histogramas e percentis configuráveis.

### Logging
- Correlation ID automático via **MDC**.
- Integração nativa com spans de tracing.

## 7. Dependências OpenCensus

```xml
<!-- OpenCensus -->
<dependency>
    <groupId>io.opencensus</groupId>
    <artifactId>opencensus-api</artifactId>
    <version>0.31.1</version>
</dependency>
<dependency>
    <groupId>io.opencensus</groupId>
    <artifactId>opencensus-impl</artifactId>
    <version>0.31.1</version>
</dependency>
<dependency>
    <groupId>io.opencensus</groupId>
    <artifactId>opencensus-exporter-trace-jaeger</artifactId>
    <version>0.31.1</version>
</dependency>

<!-- Micrometer -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
    <version>1.10.0</version>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.10.0</version>
</dependency>
```

## 8. Benefícios da Nova Implementação

### Estabilidade
- **OpenCensus** é maduro, estável e compatível com **Java 8**.
- Suporte a sistemas legados.

### Eficiência
- Coleta leve com baixo impacto no desempenho.
- Amostragem adaptativa reduz custo em produção.

### Integração
- Compatível com **Jaeger**, **Prometheus**, **Zipkin**.
- Configuração simplificada por variáveis de ambiente.

### Operacionalização
- Dashboards prontos para métricas JVM.
- Traces completos com **Correlation ID**.
- Alertas configuráveis via Prometheus.

### Experiência do Desenvolvedor
- API simples via `ObservabilityUtils`.
- Configuração automatizada.
- Logs já correlacionados com spans.

'''
Arquitetura de Observabilidade Completa:

1. TracingConfig (OpenTelemetry + Jaeger):
   - Configura exportação OTLP para Jaeger
   - Integração com Micrometer Tracing
   - Propagação de contexto W3C Trace Context
   - Sampling 100% para ambientes de desenvolvimento

2. LoggingAspect (Logs Estruturados):
   - AspectJ para logging automático de métodos
   - Correlação de logs com tracing (traceId, spanId)
   - Registro de entrada/saída e tempo de execução
   - Tratamento de erros com stack traces
   - MDC para contexto adicional

3. MetricsConfig (Micrometer + Prometheus):
   - Tags comuns para identificação de serviços
   - Filtros para métricas irrelevantes
   - Configuração de histogramas e percentis
   - Renomeação de tags para padronização
   - Suporte nativo ao Spring Boot Actuator

4. ObservabilityUtils (Utilitário):
   - Métodos para criação de métricas
   - Gerenciamento simplificado de spans
   - Registro de eventos e exceções
   - Medição de tempo de execução
   - Métricas de negócio personalizadas

5. ObservabilityConfig (Configuração Web):
   - Filtro para observabilidade HTTP
   - Configuração automática de métricas web

Como Funciona:

1. Logs:
   - Aspecto captura todos os métodos do domínio
   - Registra entrada, saída e erros
   - Adiciona traceId, spanId e logId ao contexto
   - Formato estruturado para ingestão no ELK

2. Tracing:
   - Integração com OpenTelemetry
   - Exporta traces para Jaeger via OTLP
   - Propagação automática entre serviços
   - Suporte a spans aninhados

3. Métricas:
   - Expostas via endpoint /actuator/prometheus
   - Histogramas para tempos de resposta
   - Contadores para erros e eventos
   - Gauges para métricas de negócio

Configuração do Ambiente:

application.yml:
management:
  endpoints:
    web:
      exposure:
        include: health, info, prometheus, metrics
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: ${spring.application.name}
  tracing:
    sampling:
      probability: 1.0 # 100% em dev, 0.1 em prod

otel:
  exporter:
    otlp:
      endpoint: http://jaeger-collector:4317
  resource:
    attributes:
      service.name: ${spring.application.name}

Benefícios:

1. Visibilidade Total:
   - Correlação entre logs, traces e métricas
   - Diagnóstico rápido de problemas
   - Monitoramento em tempo real

2. Desempenho:
   - Baixo overhead de instrumentação
   - Coleta eficiente de dados
   - Amostragem configurável

3. Operacionalização:
   - Dashboards unificados (Grafana)
   - Alertas baseados em SLOs
   - Detecção de anomalias

4. Padrões Abertos:
   - OpenTelemetry (CNCF)
   - Prometheus (CNCF)
   - Jaeger (CNCF)

Integração com Jaeger:
   - Visualização de traces distribuídos
   - Análise de latência entre serviços
   - Identificação de gargalos

Integração com Prometheus/Grafana:
   - Dashboards para métricas de infra e negócio
   - Alertas para erro rate, latência, etc.
   - Análise histórica de desempenho

Exemplo de Uso:

// Método instrumentado automaticamente pelo LoggingAspect
public Payment processPayment(PaymentRequest request) {
    // Criação manual de span
    Span span = observabilityUtils.startSpan("process-payment");
    try (Tracer.SpanInScope scope = tracer.withSpan(span)) {
        // Registro de métrica de negócio
        observabilityUtils.addBusinessMetric("payment.amount", 
            request.getAmount().doubleValue(),
            "currency", request.getCurrency());
        
        // Processamento...
        observabilityUtils.logEvent("payment.processed", Map.of(
            "transactionId", result.getId()
        ));
        
        return result;
    } catch (Exception ex) {
        observabilityUtils.recordException(ex, "processing_error", "payment");
        throw ex;
    } finally {
        observabilityUtils.endSpan(span);
    }
}
'''
# passo_16_impl_observabilidade.py
'''
As classes serão geradas na estrutura:

    common/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── pagamento/
                        └── common/
                            └── observability/
                                ├── TracingConfig.java
                                ├── LoggingAspect.java
                                ├── MetricsConfig.java
                                ├── ObservabilityUtils.java
                                └── ObservabilityConfig.java

Componentes principais:

1. TracingConfig.java

    Configura o tracing distribuído com OpenTelemetry
    Exporta traces para Jaeger via OTLP
    Suporte a propagação de contexto W3C
    Sampling configurável (100% em dev, 10% em prod)

2. LoggingAspect.java
    Aspecto para logging automático de métodos
    Adiciona traceId e spanId a todos os logs
    Medição de tempo de execução
    Tratamento de erros com stack traces
    MDC para contexto adicional

3. MetricsConfig.java
    Configuração avançada de métricas
    Tags comuns para todos os serviços
    Histogramas para tempos de resposta
    Filtros para endpoints irrelevantes
    Suporte a Prometheus

4. ObservabilityUtils.java
    Utilitário para instrumentação manual
    Criação de contadores e timers
    Gerenciamento de spans
    Registro de eventos e exceções
    Métricas de negócio personalizadas

5. ObservabilityConfig.java
    Configuração web para observabilidade
    Filtro HTTP para captura automática
    Integração com Spring MVC

Fluxo de Observabilidade:
    Logs Estruturados:
        Capturados automaticamente pelo aspecto
        Correlacionados com traces via traceId
        Enriquecidos com contexto adicional
        Formatados para ingestão no ELK Stack

    Tracing Distribuído:
        Spans automáticos para requisições HTTP
        Propagação entre serviços via headers
        Exportação para Jaeger
        Visualização de fluxos transacionais

    Métricas:
        Coletadas automaticamente pelo Spring Actuator
        Expostas em formato Prometheus
        Dashboard no Grafana para visualização
        Alertas configuráveis

Benefícios:
    Diagnóstico Rápido: Correlação completa de eventos
    Monitoramento Proativo: Identificação de problemas antes de afetar usuários
    Análise de Desempenho: Identificação de gargalos
    Conformidade: Auditoria completa de transações
    Otimização: Insights para melhorias de desempenho

Esta implementação fornece uma base sólida para os três pilares da observabilidade moderna, permitindo que você monitore, analise e otimize seu sistema de pagamentos de forma abrangente.

'''
