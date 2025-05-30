# MICRO SERVIÇO  pix-service
pagamento-system\pix-service

## Setup
1. `mvn clean install`
2. `java -jar target/pix-service.jar`

## Endpoints
- `POST /pix/create`
- `GET /pix/status/{id}`

###

Características da implementação:

    Validação avançada de chaves PIX:
        Suporte a CPF (11 dígitos)
        Email (formato básico)
        Telefone (com DDD)
        Chave aleatória (UUID)

    Geração de QR Code:
        Gera uma representação simplificada do payload PIX
        Formato: PIX|{chave}|{valor}|{beneficiario}|{id}

  Fluxo de processamento:
           
            sequenceDiagram
    participant Cliente
    participant Controller
    participant Service
    
    Cliente->>Controller: POST /pix/pagar (PixRequest)
    Controller->>Service: processarPix()
    Service->>Service: Validar chave PIX
    alt Chave inválida
        Service-->>Controller: Resposta de erro
    else Chave válida
        Service->>Service: Gerar QR Code
        Service->>Service: Simular processamento
        alt Sucesso
            Service-->>Controller: PixResponse com sucesso
        else Falha
            Service-->>Controller: PixResponse com falha
        end
    end
    Controller-->>Cliente: Resposta HTTP
  
  
Tratamento de erros:
        3% de chance de falha no processamento
        Validação de valor positivo
        Verificação de formato de chave

Para testar o serviço:
    Gere os arquivos:

bash

python passo_5_impl_pagamento_pix.py

    Execute o serviço:
bash

cd pix-service
mvn spring-boot:run

    Teste com diferentes tipos de chaves PIX:

CPF:
bash

curl -X POST http://localhost:8080/pix/pagar \
     -H "Content-Type: application/json" \
     -d '{
          "chave": "12345678901",
          "valor": 150.99,
          "descricao": "Pagamento de serviços",
          "beneficiario": "Empresa XYZ"
     }'

Email:
bash

curl -X POST http://localhost:8080/pix/pagar \
     -H "Content-Type: application/json" \
     -d '{
          "chave": "cliente@example.com",
          "valor": 75.50,
          "descricao": "Compra online",
          "beneficiario": "Loja ABC"
     }'

Telefone:
bash

curl -X POST http://localhost:8080/pix/pagar \
     -H "Content-Type: application/json" \
     -d '{
          "chave": "5511999999999",
          "valor": 200.00,
          "descricao": "Recarga de créditos",
          "beneficiario": "Telecom"
     }'

Chave aleatória (UUID):
bash

curl -X POST http://localhost:8080/pix/pagar \
     -H "Content-Type: application/json" \
     -d '{
          "chave": "a1b2c3d4-e5f6-7a8b-9c0d-ef1234567890",
          "valor": 300.00,
          "descricao": "Doação",
          "beneficiario": "ONG ABC"
     }'

Resposta esperada (sucesso):
json

{
  "idTransacao": "d3d94468-2e2a-4b7d-9c8f-1a6f7a3c4b5d",
  "chave": "12345678901",
  "valor": 150.99,
  "dataHora": "2025-05-29T17:45:30.123",
  "status": "CONCLUIDO",
  "qrCode": "PIX|12345678901|150.99|Empresa XYZ|a1b2c3d4",
  "mensagem": "Pagamento PIX realizado com sucesso"
}

Casos de erro:

Chave inválida:
json

{
  "idTransacao": null,
  "chave": "chave_invalida",
  "valor": 0.0,
  "dataHora": "2025-05-29T17:46:15.456",
  "status": "REJEITADO",
  "qrCode": null,
  "mensagem": "Chave PIX inválida"
}

Valor inválido:
json

{
  "idTransacao": null,
  "chave": "12345678901",
  "valor": 0.0,
  "dataHora": "2025-05-29T17:47:22.789",
  "status": "REJEITADO",
  "qrCode": null,
  "mensagem": "Valor deve ser positivo"
}

Falha no processamento:
json

{
  "idTransacao": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
  "chave": "12345678901",
  "valor": 150.99,
  "dataHora": "2025-05-29T17:48:05.321",
  "status": "FALHA",
  "qrCode": "PIX|12345678901|150.99|Empresa XYZ|e5f6g7h8",
  "mensagem": "Falha no processamento PIX"
}

Esta implementação está pronta para ser integrada com:

    Sistemas bancários reais usando APIs PIX
    Geração de QR Codes visuais (usando bibliotecas como ZXing)
    Notificações em tempo real
    Sistemas de conciliação bancária
    
'''
