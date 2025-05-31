Arquitetura de Segurança com TLS:

1. Certificados Gerados:
   - server.keystore: Armazena a chave privada e certificado do servidor
   - server.truststore: Armazena certificados de autoridades confiáveis (CA)
   - Autoassinados para desenvolvimento (em produção usar certificados de CA confiável)

2. Configurações Principais:
   - TLS 1.3 com cifras fortes (AES_256_GCM, CHACHA20_POLY1305)
   - Autenticação mútua (client-auth: need)
   - Redirecionamento HTTP para HTTPS
   - HSTS com max-age=1 ano e includeSubDomains
   - Política de Segurança de Conteúdo (CSP) restritiva
   - Proteção contra XSS e clickjacking

3. Componentes de Segurança:
   - SecurityConfig: Configuração do Spring Security
   - TlsConfig: Configuração do Tomcat para HTTP/HTTPS

Como Funciona:

1. Handshake TLS:
   - Cliente e servidor negociam versão TLS e cifras
   - Servidor apresenta certificado (do keystore)
   - Cliente valida certificado contra truststore
   - Cliente opcionalmente apresenta certificado (autenticação mútua)

2. Fluxo HTTP/HTTPS:
   - Conexões HTTP na porta 8080 são redirecionadas para HTTPS na porta 8443
   - Header HSTS instrui navegadores a usar apenas HTTPS

3. Proteções Adicionais:
   - CSRF: Tokens em cookies HttpOnly
   - CSP: Restringe fontes de conteúdo executável
   - XSS-Protection: Bloqueia scripts maliciosos
   - Frame-Options: Prevenção contra clickjacking

Configuração de Produção:

1. Certificados:
   - Obter certificados de uma CA pública (Let's Encrypt, DigiCert, etc.)
   - Renovar periodicamente (automatizar com certbot)
   - Usar certificados wildcard para subdomínios

2. Application.yml:
   - Alterar caminhos dos keystore/truststore
   - Usar senhas seguras (via variáveis de ambiente)
   - Habilitar apenas TLS 1.3 em produção

3. Hardening:
   - Configurar OCSP stapling
   - Habilitar HTTP Strict Transport Security (HSTS)
   - Implementar Certificate Transparency
   - Configurar políticas de cipher suites

Benefícios da Implementação:

1. Confidencialidade:
   - Dados criptografados em trânsito
   - Proteção contra eavesdropping

2. Integridade:
   - Prevenção contra alteração de dados
   - Detecção de tampering

3. Autenticação:
   - Verificação de identidade do servidor
   - Autenticação mútua opcional para clientes

4. Conformidade:
   - Adequação a LGPD, GDPR, PCI DSS
   - Práticas recomendadas de segurança

5. Proteção Avançada:
   - Mitigação de ataques MITM
   - Prevenção contra downgrade attacks
   - Proteção contra vulnerabilidades conhecidas

Uso no Kubernetes:

1. Secrets:
   - Criar secrets para keystore/truststore
   - Montar como volumes nos pods

2. Ingress:
   - Configurar TLS termination no ingress controller
   - Usar certificados gerenciados pelo cluster

apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: payment-ingress
  annotations:
    nginx.ingress.kubernetes.io/backend-protocol: "HTTPS"
spec:
  tls:
  - hosts:
    - pagamento.com
    secretName: pagamento-tls-secret
  rules:
  - host: pagamento.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: payment-service
            port:
              number: 8443

Esta implementação fornece uma base sólida para segurança em trânsito, garantindo que todas as comunicações
com o sistema de pagamentos sejam criptografadas e autenticadas, protegendo dados sensíveis como informações
de cartão de crédito e dados pessoais dos usuários.
'''
# passo_19_impl_TLS_seguranca.py

'''
Os certificados e configurações serão gerados na estrutura:

    common/
    └── src/
        ├── main/
        │   ├── java/
        │   │   └── com/
        │   │       └── pagamento/
        │   │           └── common/
        │   │               └── security/
        │   │                   ├── SecurityConfig.java
        │   │                   └── TlsConfig.java
        │   └── resources/
        │       ├── certs/
        │       │   ├── server.keystore
        │       │   └── server.truststore
        │       └── application-security.yml

Componentes principais:

1. Certificados TLS:

    server.keystore: Armazena chave privada e certificado do servidor
    server.truststore: Armazena certificados de autoridades confiáveis
    Gerados automaticamente com OpenSSL e keytool
    Configuração autoassinada para desenvolvimento

2. SecurityConfig.java:
    Configuração do Spring Security
    Habilita HTTPS obrigatório
    Políticas de segurança:
        HSTS (HTTP Strict Transport Security)
        CSP (Content Security Policy)
        XSS Protection
        Frame Options (prevenção clickjacking)
        CSRF Protection

    Autenticação básica e form login

3. TlsConfig.java:

    Configura o Tomcat para suportar HTTP e HTTPS
    Redireciona todas as requisições HTTP para HTTPS
    Configura portas personalizadas (8080 para HTTP, 8443 para HTTPS)

4. application-security.yml:

    Configuração detalhada do TLS:
       Versão: TLSv1.3
        Cifras: TLS_AES_256_GCM_SHA384, TLS_CHACHA20_POLY1305_SHA256
        Autenticação mútua
        Referência aos keystores gerados

    Configurações de hardening de segurança

Características de Segurança:

    Criptografia Forte:
        TLS 1.3 com cifras modernas
        Chaves RSA 2048 bits
        Perfect Forward Secrecy

    Proteções HTTP:

        HSTS com max-age=1 ano
        CSP restritivo
        XSS-Protection em modo bloqueio
        Frame-Options: DENY

    Autenticação:

        Autenticação mútua TLS (opcional)
        CSRF protection com tokens
        Spring Security para controle de acesso

    Configuração Segura:

        Desabilitação de protocolos inseguros (SSLv2, SSLv3, TLS 1.0, TLS 1.1)
        Cifras fortes priorizadas
        Senhas protegidas por variáveis de ambiente em produção

Fluxo de Comunicação Segura:
Diagram
Code

sequenceDiagram
    participant Client
    participant Server
    participant CA
    
    Client->>Server: ClientHello (TLS 1.3)
    Server->>Client: ServerHello + Certificate
    Client->>CA: Verifica certificado
    CA-->>Client: Validação
    Client->>Server: Chave pré-mestre criptografada
    Server->>Client: Confirmação TLS
    Client->>Server: Requisição HTTP criptografada
    Server->>Client: Resposta HTTP criptografada

Benefícios:

    Proteção de Dados: Criptografia de ponta-a-ponta para dados sensíveis
    Autenticidade: Verificação da identidade do servidor
    Integridade: Detecção de adulteração de dados
    Conformidade: Adequação a padrões de segurança (PCI DSS, LGPD)
    Prevenção de Ataques: Mitigação de MITM, downgrade attacks, e outros

Esta implementação garante que todas as comunicações com o sistema de pagamentos sejam realizadas através de canais seguros, protegendo informações críticas contra interceptação e adulteração.


