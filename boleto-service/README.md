# Micro Serviço - boleto-service

## Setup
1. `mvn clean install`
2. `java -jar target/boleto-service.jar`

## Endpoints
- `POST /boleto/create`
- `GET /boleto/status/{id}`

Características da implementação:
#   Modelagem de dados:
       Boleto.java: Contém informações do boleto (código de barras, valor, vencimento, etc.)
       BoletoResponse.java: Resposta com resultado do processamento

   Validações:
       Formato do código de barras (44 caracteres)
       Verificação de vencimento
       Validação de valor positivo

   Simulações:
       Geração de número de controle
       5% de chance de falha no processamento bancário
       Verificação de vencimento

# Para testar:
   cd pagamento-system/boleto-service
   mvn spring-boot:run

   curl -X POST http://localhost:8080/boletos/pagar \
        -H "Content-Type: application/json" \
        -d '{
             "codigoBarras": "00190500954014481606906809350314337370000000100",
             "valor": 150.99,
             "dataVencimento": "2025-12-31",
             "beneficiario": "Empresa XYZ",
             "pagador": "Cliente ABC"
        }'

# Resposta esperada:
   {
     "codigoBarras": "00190500954014481606906809350314337370000000100",
     "numeroControle": "CTRL0123456789",
     "dataPagamento": "2025-05-29T15:30:45.123",
     "status": "PAGO",
     "mensagem": "Pagamento realizado com sucesso"
   }

# Casos de erro tratados:
   Boleto vencido:
     {"status": "REJEITADO", "mensagem": "Boleto inválido ou vencido"}

   Formato inválido:
     {"status": "REJEITADO", "mensagem": "Boleto inválido ou vencido"}

#   Falha no banco:
     {"status": "FALHA", "mensagem": "Falha no processamento do banco"}

# Esta implementação está pronta para ser integrada com:
   Leitura de QR Code
   Sistemas bancários reais
   Geração de PDF de comprovante
   Integração com serviço de notificação