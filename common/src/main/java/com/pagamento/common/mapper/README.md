Modulo DTO e MAPPER 

Características da implementação:
    DTOs (Data Transfer Objects):
        UserDTO: Representação segura do usuário (sem password)
        PaymentDTO: Informações essenciais de pagamento
        PixDTO: Dados específicos para transações PIX
        CardDTO: Dados para pagamentos com cartão

    Mappers com MapStruct:

Diagram
Code

classDiagram
    class User {
        +UUID id
        +String username
        +String email
        +String passwordHash
        +String role
        +List~PaymentMethod~ paymentMethods
    }
    
    class UserDTO {
        +String id
        +String username
        +String email
        +String role
        +List~String~ paymentMethods
    }
    
    class UserMapper {
        +toDTO(User user) UserDTO
    }
    
    User --> UserDTO : Mapeado por
    UserMapper ..> User : Usa
    UserMapper ..> UserDTO : Gera

    Mapeamentos inteligentes:

        Conversão automática de tipos
        Expressões customizadas (ex: formatação de paymentMethods)
        Mapeamento de propriedades aninhadas

Benefícios desta abordagem:

    Segurança:

        Não expõe dados sensíveis (como senhas)
        Controle preciso dos campos expostos

    Desacoplamento:

        Separa modelo interno de representação externa
        Permite evolução independente das APIs

    Performance:

        MapStruct gera código nativo (sem reflection)
        Compilação-time mapping (zero overhead)

    Manutenibilidade:

        Centralização das regras de mapeamento
        Código gerado automaticamente

Como usar os mappers:

Exemplo de conversão de User para UserDTO:
java

// Injetar o mapper
@Autowired
private UserMapper userMapper;

public UserDTO getUserDTO(String userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return userMapper.toDTO(user);
}

Exemplo de resposta JSON:
json

{
  "id": "a1b2c3d4-e5f6-7a8b-9c0d-ef1234567890",
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER",
  "paymentMethods": [
    "CREDIT_CARD - abcd1234",
    "PIX - efgh5678"
  ]
}

Configuração do MapStruct no pom.xml:

    Adicionada dependência do MapStruct
    Configurado processador de anotações
    Versão definida nas propriedades

Práticas recomendadas:

    Para entidades complexas:
        Criar DTOs especializados
        Usar estratégias de mapeamento customizado

    Para coleções:

        Adicionar métodos default nos mappers
        Usar streams para transformações

    Para performance crítica:

        Usar @Mapping(target = "prop", ignore = true) para campos não utilizados
        Habilitar injeção de dependência com componentModel = "spring"

    Para validação:

        Adicionar anotações Bean Validation nos DTOs
        Validar antes do mapeamento

Este módulo fornece uma base sólida para transferência segura e eficiente de dados entre as camadas da aplicação e os microsserviços.

