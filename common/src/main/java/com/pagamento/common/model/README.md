# Modulo Common 

Este módulo comum serve como base para toda a arquitetura de microsserviços, 
garantindo consistência e facilitando a integração entre os diferentes componentes 
do sistema de pagamentos.

Para usar este módulo em outros projetos, adicione a dependência no pom.

# Características das entidades implementadas:

    User.java:

        Entidade principal do usuário
        Relacionamento 1-N com PaymentMethod
        Campos: id, username, email, passwordHash, role
        UUID como chave primária

    PaymentMethod.java:

        Métodos de pagamento associados ao usuário
        Tipos: CREDIT_CARD, DEBIT_CARD, PIX, BOLETO
        Campo "details" para armazenar dados específicos em JSON
        Relacionamento N-1 com User

    Payment.java:

        Entidade base para todos os pagamentos
        Status: CREATED, PENDING, COMPLETED, FAILED
        Relacionamentos com User e PaymentMethod
        Atualização automática de timestamps

    AuditLog.java:

        Registro de auditoria para todas as operações críticas
        Ações: CREATE, UPDATE, DELETE
        Registra quem executou a ação e quando

# Diagrama de entidades:
Diagram
Code

erDiagram
    USER ||--o{ PAYMENT_METHOD : has
    USER ||--o{ PAYMENT : makes
    PAYMENT_METHOD ||--o{ PAYMENT : used_in
    PAYMENT ||--o{ AUDIT_LOG : triggers

    USER {
        UUID id PK
        string username
        string email
        string passwordHash
        string role
    }
    
    PAYMENT_METHOD {
        UUID id PK
        string type
        string details
        UUID user_id FK
    }
    
    PAYMENT {
        UUID id PK
        decimal amount
        string currency
        string status
        datetime createdAt
        datetime updatedAt
        UUID user_id FK
        UUID payment_method_id FK
    }
    
    AUDIT_LOG {
        UUID id PK
        string entityName
        string entityId
        string action
        string details
        string performedBy
        datetime performedAt
    }

# Depedências do Maven:

    Jakarta Persistence API (JPA 3.1)
    UUID Generator
    Hibernate Core (para suporte a tipos avançados)

Como usar em outros microsserviços:

    Adicione o módulo como dependência no pom.xml:

xml

<dependency>
    <groupId>com.pagamento</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>

    Importe as entidades nos serviços:

java

import com.pagamento.common.model.User;
import com.pagamento.common.model.Payment;

# Benefícios desta abordagem:

    Consistência:
        Mesmas entidades em todos os serviços
        Validações centralizadas
        Formato padronizado de dados

    Manutenibilidade:
        Atualizações em um único lugar
        Evolução compartilhada do modelo
        Redução de duplicação

    Interoperabilidade:
        Facilita comunicação entre serviços
        Contratos de dados bem definidos
        Suporte a eventos de domínio

    Auditoria:

        Rastreabilidade de operações
        Compliance com regulamentações
        Histórico completo de alterações


Este módulo comum serve como base para toda a arquitetura de microsserviços, 
garantindo consistência e facilitando a integração entre os diferentes componentes 
do sistema de pagamentos.
Para usar este módulo em outros projetos, adicione a dependência no pom.

xml

<dependency>
    <groupId>com.pagamento</groupId>
    <artifactId>common</artifactId>
    <version>1.0.0</version>
</dependency>

E importe as entidades em seus serviços:
java

import com.pagamento.common.model.User;
import com.pagamento.common.model.Payment;
import com.pagamento.common.model.PaymentMethod;
import com.pagamento.common.model.AuditLog;

Este módulo fornece a base para toda a modelagem de dados do 
sistema de pagamentos, garantindo consistência e integridade
 entre os diferentes microsserviços.
