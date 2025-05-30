# Micro Seviço gateway-service

Este microsserviço atua como uma camada de abstração unificada
 para processamento de pagamentos através de múltiplas plataformas, 
 simplificando a integração para sistemas clientes.
O serviço estará disponível em: http://localhost:8084/gateway/payments

# Exemplo de teste:
bash

curl -X POST http://localhost:8084/gateway/payments \
  -H "Content-Type: application/json" \
  -d '{
    "provider": "PAGSEGURO",
    "amount": 150.99,
    "currency": "BRL",
    "referenceId": "ORD12345",
    "customerId": "CLI67890",
    "paymentMethod": "CREDIT_CARD",
    "metadata": "{\"card\": {\"number\": \"4111111111111111\", \"expiry\": \"12/30\", \"cvv\": \"123\"}}"
  }'


# Características da implementação:

    Arquitetura flexível:

Diagram
Code

classDiagram
    class PaymentGateway {
        <<interface>>
        +processPayment(GatewayPaymentRequest) GatewayPaymentResponse
        +supports(String) boolean
    }
    
    class PicPayGateway
    class PagSeguroGateway
    class MercadoPagoGateway
    class PayPalGateway
    
    PaymentGateway <|.. PicPayGateway
    PaymentGateway <|.. PagSeguroGateway
    PaymentGateway <|.. MercadoPagoGateway
    PaymentGateway <|.. PayPalGateway
    
    class GatewayFactory {
        +getGateway(String) PaymentGateway
    }
    
    class GatewayService {
        +processPayment(GatewayPaymentRequest) GatewayPaymentResponse
    }
    
    GatewayService --> GatewayFactory
    GatewayFactory --> PaymentGateway

  #  Suporte a múltiplos providers:

        PicPay
        PagSeguro
        MercadoPago
        PayPal

        Fácil adição de novos gateways

   # Padrões de projeto:

        Strategy Pattern: Implementações específicas por gateway
        Factory Pattern: Seleção dinâmica do gateway
        Dependency Injection: Gerenciamento de dependências

   # Configuração centralizada:

        Provider padrão
        Taxa de processamento
        Timeout de operações

   # Simulações realistas:

        Taxas de falha diferentes por provider
        Geração de IDs de transação
        Respostas específicas de cada gateway

# Fluxo de funcionamento:
Diagram
Code

sequenceDiagram
    participant Cliente
    participant Controller
    participant GatewayService
    participant GatewayFactory
    participant PaymentGateway
    
    Cliente->>Controller: POST /gateway/payments (JSON)
    Controller->>GatewayService: processPayment(request)
    GatewayService->>GatewayFactory: getGateway(provider)
    GatewayFactory-->>GatewayService: PaymentGateway
    GatewayService->>PaymentGateway: processPayment(request)
    PaymentGateway-->>GatewayService: Response
    GatewayService-->>Controller: Response
    Controller-->>Cliente: HTTP 200 + JSON

# Para testar o serviço:

cd gateway-service
mvn spring-boot:run

    Teste com diferentes providers:

PagSeguro:
bash

curl -X POST http://localhost:8080/gateway/payments \
     -H "Content-Type: application/json" \
     -d '{
          "provider": "PAGSEGURO",
          "amount": 150.99,
          "currency": "BRL",
          "paymentMethod": "CREDIT_CARD",
          "referenceId": "ORDER-12345",
          "customerId": "CUST-67890"
     }'

PicPay:
bash

curl -X POST http://localhost:8080/gateway/payments \
     -H "Content-Type: application/json" \
     -d '{
          "provider": "PICPAY",
          "amount": 75.50,
          "currency": "BRL",
          "paymentMethod": "PIX",
          "referenceId": "ORDER-54321",
          "customerId": "CUST-09876"
     }'

Usando provider padrão:
bash

curl -X POST http://localhost:8080/gateway/payments \
     -H "Content-Type: application/json" \
     -d '{
          "amount": 200.00,
          "currency": "BRL",
          "paymentMethod": "BOLETO",
          "referenceId": "ORDER-11223",
          "customerId": "CUST-44556"
     }'

Resposta de sucesso (exemplo):
json

{
  "transactionId": "PAG1693423456789123",
  "provider": "PAGSEGURO",
  "status": "COMPLETED",
  "message": "Payment processed successfully by PagSeguro",
  "timestamp": "2025-05-29T18:30:45.123",
  "providerResponse": "AuthorizationCode: PAG123456",
  "success": true
}

Resposta de erro (exemplo):
json

{
  "transactionId": "PP1693423456789456",
  "provider": "PAYPAL",
  "status": "FAILED",
  "message": "PayPal processing error",
  "timestamp": "2025-05-29T18:31:22.456",
  "providerResponse": "ErrorCode: PP-500",
  "success": false
}

Vantagens da abordagem:

    Desacoplamento:
        Adição/remoção de gateways sem afetar o core
        Atualizações independentes por provider

    Resiliência:
        Circuit breaker (a ser implementado)
        Retry policies
        Fallback mechanisms

    Monitoramento:
        Métricas por provider
        Logs detalhados
        Dashboard de performance

    Extensibilidade:
        Fácil adição de novos gateways
        Suporte a diferentes métodos de pagamento
        Internacionalização

Este microsserviço atua como uma camada de abstração unificada
 para processamento de pagamentos através de múltiplas plataformas, 
 simplificando a integração para sistemas clientes.

O serviço estará disponível em: http://localhost:8084/gateway/payments

# Exemplo de teste:
bash

curl -X POST http://localhost:8084/gateway/payments \
  -H "Content-Type: application/json" \
  -d '{
    "provider": "PAGSEGURO",
    "amount": 150.99,
    "currency": "BRL",
    "referenceId": "ORD12345",
    "customerId": "CLI67890",
    "paymentMethod": "CREDIT_CARD",
    "metadata": "{\"card\": {\"number\": \"4111111111111111\", \"expiry\": \"12/30\", \"cvv\": \"123\"}}"
  }'
