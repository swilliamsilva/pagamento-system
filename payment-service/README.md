# MICRO SERVIÇO 
caminho: pagamento-system\payment-service
# payment-service

## Setup
1. `mvn clean install`
2. `java -jar target/payment-service.jar`

## Endpoints
- `POST /payment/create`
- `GET /payment/status/{id}`
##

Este Micro Serviço é o orquestrador de pagamentos da sua arquitetura de microserviços. 
Ele coordena o fluxo de pagamento de ponta a ponta, independente do meio de pagamento (Pix, boleto, cartão, etc).
________________________________________
Função principal

Atuar como coordenador de transações financeiras, delegando a execução para serviços especializados (Pix, Boleto, Cartão, etc), conforme o tipo de pagamento solicitado.
________________________________________Fluxo esperado (orquestração)
1.	Recebe a requisição de pagamento
o	Entrada comum via POST /payments com dados genéricos (amount, currency, type, etc).
2.	Decide qual serviço invocar
o	Ex: Se type = PIX, ele chama pix-service.
o	Se type = BOLETO, chama boleto-service.
3.	Encaminha a requisição para o microserviço especializado
o	Pode ser via REST, mensageria, ou outra forma configurada.
4.	Aguarda ou trata a resposta
o	Recebe um status do serviço externo (ex: PENDING, PAID, ERROR).
5.	Atualiza o status interno da transação
o	Salva um log/persistência com o status do pagamento (via JPA ou mensageria).
6.	Retorna resposta ao cliente
o	Com status da transação, id gerado, dados adicionais (ex: QR code do Pix, linha digitável do boleto, etc).
________________________________________Serviços que ele orquestra (dependências)
O pagamento-system depende logicamente de:
Serviço	Finalidade	Meio de integração sugerido
pix-service	Geração e confirmação de pagamento Pix	REST ou Kafka
boleto-service	Emissão e status de boletos	REST
cartao-service	Processamento de débito/crédito	REST
auth-service	Verificação de autenticação/autorização	JWT/REST (opcional)
gateway-service	Integrações com serviços externos (Asaas, PagSeguro)	REST/Async

