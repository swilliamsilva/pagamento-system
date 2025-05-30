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