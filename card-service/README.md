# card-service

## Setup
1. `mvn clean install`
2. `java -jar target/card-service.jar`

## Endpoints
- `POST /card/create`
- `GET /card/status/{id}`
Características da implementação:

    Validações robustas:
        Formato do número do cartão (16 dígitos)
        Formato do CVV (3 ou 4 dígitos)
        Formato da data de expiração (MM/AA)
        Verificação de cartão expirado
        Simulação de saldo insuficiente

    Fluxo de processamento:

Diagram
Code

graph TD
    A[Receber requisição] --> B{Validar dados}
    B -->|Inválido| C[Rejeitar pagamento]
    B -->|Válido| D{Cartão expirado?}
    D -->|Sim| E[Rejeitar pagamento]
    D -->|Não| F{Processar com operadora}
    F -->|Falha| G[Retornar falha]
    F -->|Saldo insuficiente| H[Rejeitar pagamento]
    F -->|Sucesso| I[Aprovar pagamento]

    Simulações realistas:

        4% de chance de falha no processamento
        10% de chance de saldo insuficiente
        Geração de código de autorização

    Tratamento de expiração:

        Converte MM/AA para data completa
        Considera o último dia do mês
        Verifica se a data é anterior à data atual

Para testar o serviço:

    Gere os arquivos:

bash

python passo_6_impl_pagamento_cartao.py

    Execute o serviço:

bash

cd card-service
mvn spring-boot:run

    Teste com uma requisição válida:

bash

curl -X POST http://localhost:8080/debit-cards/payments \
     -H "Content-Type: application/json" \
     -d '{
          "cardNumber": "4111111111111111",
          "cardHolder": "FULANO DA SILVA",
          "expiryDate": "12/30",
          "cvv": "123",
          "amount": 150.99,
          "description": "Compra de eletrônicos"
     }'

Respostas possíveis:

Pagamento aprovado:
json

{
  "transactionId": "d3d94468-2e2a-4b7d-9c8f-1a6f7a3c4b5d",
  "status": "APROVADO",
  "message": "Pagamento realizado com sucesso",
  "timestamp": "2025-05-29T17:45:30.123",
  "authorizationCode": "AUTH123456"
}

Cartão expirado:
json

{
  "transactionId": null,
  "status": "REJEITADO",
  "message": "Cartão expirado",
  "timestamp": "2025-05-29T17:46:15.456",
  "authorizationCode": null
}

Dados inválidos:
json

{
  "transactionId": null,
  "status": "REJEITADO",
  "message": "Dados do cartão inválidos",
  "timestamp": "2025-05-29T17:47:22.789",
  "authorizationCode": null
}

Saldo insuficiente:
json

{
  "transactionId": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "status": "REJEITADO",
  "message": "Saldo insuficiente",
  "timestamp": "2025-05-29T17:48:05.321",
  "authorizationCode": null
}

Falha no processamento:
json

{
  "transactionId": "a1b2c3d4-e5f6-7a8b-9c0d-ef1234567890",
  "status": "FALHA",
  "message": "Falha no processamento com a operadora",
  "timestamp": "2025-05-29T17:49:15.654",
  "authorizationCode": null
}

Melhorias futuras:

    Criptografia de dados sensíveis:
        Armazenar números de cartão de forma segura
        Usar tokens em vez de dados reais

    Integração com gateways reais:
        Adicionar SDKs de processadores de pagamento
        Implementar circuit breaker para resiliência

    PCI Compliance:
        Validar conformidade com padrões de segurança
        Auditoria regular de segurança

    Salvamento de transações:
        Persistir dados em banco para reconciliação
        Implementar histórico de transações

Este microsserviço está pronto para ser integrado ao sistema principal 
de pagamentos e pode ser facilmente estendido para suportar cartões 
de crédito com pequenas modificações.
