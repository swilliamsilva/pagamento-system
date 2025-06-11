## Integração Asaas

### Fluxo Principal
```mermaid
sequenceDiagram
    participant Client as Cliente
    participant Controller as PaymentController
    participant Adapter as AsaasPaymentAdapter
    participant Asaas as API Asaas

    Client->>Controller: Envia PaymentMethodDTO
    Controller->>Adapter: toAsaasRequest(dto)
    Adapter->>Asaas: Envia AsaasPaymentRequest
    Asaas-->>Adapter: Retorno
    Adapter-->>Controller: Resposta
    Controller-->>Client: Confirmação
```

### Dependências
| Classe               | Tipo        | Descrição                             |
|----------------------|-------------|---------------------------------------|
| `PaymentMethodDTO`   | Consumo     | Entrada do core application           |
| `AsaasPaymentRequest`| Produção    | Saída para API externa                |
| `AsaasPaymentClient` | Colaboração | Cliente HTTP para envio               |

### Especificações Técnicas
- **Padrão**: Adapter Pattern
- **Thread Safety**: Stateless (thread-safe)
- **Cache**: Não recomendado (dados sensíveis)