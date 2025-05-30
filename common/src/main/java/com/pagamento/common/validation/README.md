Arquitetura de Validação:

1. Validação com Bean Validation (JSR 380):
   - CPFValidator: Implementa validação de CPF com algoritmo oficial
   - AmountValidator: Valida valores monetários (positivos e dentro de limites)
   
2. Anotações customizadas:
   - @ValidCPF: Para marcar campos que precisam de validação de CPF
   - @ValidAmount: Para validar valores monetários com parâmetros configuráveis

3. Utilitário de validações comuns:
   - ValidationUtils: Oferece métodos estáticos para várias validações:
     • Email: Valida formato de e-mail
     • CNPJ: Valida formato e tamanho de CNPJ
     • Código de Barras: Valida formato padrão (44 dígitos)
     • QR Code: Valida formato base64
     • Data: Valida formato ISO (YYYY-MM-DD)
     • CEP: Valida formato de CEP brasileiro

Como usar as validações:

Exemplo em entidades:

public class PaymentRequest {
    @ValidCPF
    private String payerCpf;
    
    @ValidAmount(min = 0.01, max = 10000.00)
    private BigDecimal amount;
}

Exemplo em serviços:

if (!ValidationUtils.isValidEmail(user.getEmail())) {
    throw new IllegalArgumentException("Email inválido");
}

Benefícios:

1. Consistência: Validações padronizadas em toda a aplicação
2. Reúso: Lógica centralizada evitando duplicação
3. Flexibilidade: Anotações com parâmetros configuráveis
4. Desacoplamento: Implementação separada das regras de negócio
5. Internacionalização: Mensagens de erro customizáveis

Configuração necessária no pom.xml:

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

Implementação detalhada:

CPFValidator:
   - Remove caracteres especiais
   - Verifica tamanho e dígitos repetidos
   - Calcula e compara dígitos verificadores
   - Trata exceções de conversão

AmountValidator:
   - Valida valor positivo
   - Verifica limites mínimo e máximo
   - Mensagens customizadas por tipo de erro
   - Configuração via parâmetros na anotação

ValidationUtils:
   - Usa expressões regulares para validação
   - Métodos estáticos para uso direto
   - Validações leves sem dependências
   
   Características principais:

    Validação de CPF completa com cálculo de dígitos verificadores

    Validação de valores monetários com limites configuráveis

    Suporte a múltiplos formatos (email, CNPJ, CEP, códigos, datas)

    Integração com Bean Validation (JSR 380)

    Mensagens de erro específicas para cada tipo de falha

    Métodos utilitários para validações programáticas

    Totalmente configurável e extensível
