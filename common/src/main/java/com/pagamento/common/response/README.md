Modulo Request Response
Características das classes geradas:

Request:
---------
1. AuthRequest:
   - Usada para autenticação de usuários
   - Campos: username (String) e password (String)
   - Construtores: padrão e parametrizado
   - Getters e Setters

2. PaymentRequest:
   - Usada para solicitação de pagamentos
   - Campos: 
        amount (BigDecimal - valor monetário)
        currency (String - moeda)
        referenceId (String - ID de referência)
   - Construtores: padrão e parametrizado
   - Getters e Setters

Response:
---------
1. AuthResponse:
   - Resposta de autenticação
   - Campos:
        authToken (String - token JWT)
        status (String - status da operação)
        message (String - mensagem descritiva)
   - Construtores: padrão e parametrizado
   - Getters e Setters

2. PaymentResponse:
   - Resposta de operações de pagamento
   - Campos:
        transactionId (String - ID único da transação)
        status (String - status do pagamento)
        code (String - código de retorno)
        timestamp (LocalDateTime - data/hora da transação)
   - Construtores: padrão e parametrizado
   - Getters e Setters

Padrões de projeto aplicados:
- JavaBeans: uso de getters/setters e construtor padrão
- DTO (Data Transfer Object): transferência de dados entre camadas
- Builder implícito: através de construtores parametrizados

Benefícios:
1. Desacoplamento: Separação clara entre modelos de request/response
2. Segurança: Controle explícito dos dados expostos nas APIs
3. Validação: Base para implementação de bean validation (ex: @NotNull)
4. Serialização: Estrutura otimizada para serialização JSON

Exemplo de uso:

@PostMapping("/auth")
public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
    // Lógica de autenticação...
    return ResponseEntity.ok(new AuthResponse("jwt.token.xyz", "SUCCESS", "Autenticação bem-sucedida"));
}

@PostMapping("/payment")
public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
    // Lógica de pagamento...
    return ResponseEntity.ok(new PaymentResponse("txn_12345", "COMPLETED", "200", LocalDateTime.now()));
}
'''