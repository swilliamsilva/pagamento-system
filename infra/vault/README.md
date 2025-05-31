Arquitetura de Gerenciamento de Segredos com Vault:

1. Inicialização do Vault:
   - Script vault-init.sh:
     • Inicializa o Vault com 5 chaves e threshold de 3
     • Desbloqueia o Vault com as chaves
     • Configura secrets engine (KV v2)
     • Habilita autenticação AppRole
     • Define política de acesso específica
     • Gera credenciais iniciais
     • Habilita auditoria

2. Política de Acesso:
   - vault-policy.hcl define permissões:
     • Acesso completo ao caminho pagamento/*
     • Acesso leitura ao caminho infra/*
     • Permissão para login via AppRole
     • Permissão para renovar leases
     • Verificar próprias capacidades

3. Configuração do Servidor:
   - vault-config.hcl:
     • Armazenamento RAFT para alta disponibilidade
     • Listener TCP na porta 8200
     • Endereços de API e cluster
     • UI habilitada

4. Kubernetes Deployment:
   - StatefulSet com volume persistente
   - ConfigMaps para configuração e políticas
   - Service para descoberta
   - Probes para health checks
   - RBAC para integração com Kubernetes Auth

5. Integração com Aplicação:
   - Classe VaultConfig.java:
     • Configura VaultTemplate com autenticação AppRole
     • Métodos para ler e escrever segredos
     • Baseado no Spring Vault

Fluxo de Trabalho:

1. Implantar Vault no Kubernetes:
   kubectl apply -f deploy-vault-k8s.yaml

2. Inicializar o Vault:
   kubectl exec -it vault-0 -n pagamento -- /bin/sh /vault/config/vault-init.sh

3. Obter credenciais:
   kubectl exec -it vault-0 -n pagamento -- cat /vault/config/credentials.env

4. Configurar segredos (exemplo):
   vault kv put secret/pagamento/db username=admin password=secret
   vault kv put secret/pagamento/jwt secretKey=super_secret_123

5. Integrar com microsserviços:
   - Configurar variáveis VAULT_URI, VAULT_ROLE_ID, VAULT_SECRET_ID
   - Usar VaultTemplate para acessar segredos

Benefícios da Implementação:

1. Segurança Fortalecida:
   - Segredos nunca armazenados em código ou repositórios
   - Criptografia em repouso e trânsito
   - Rotação automática de credenciais
   - Revogação de acesso granular

2. Gestão Centralizada:
   - Todos os segredos em um único lugar
   - Auditoria completa de acesso
   - Versionamento de segredos
   - Backups automatizados

3. Integração com Kubernetes:
   - Autenticação nativa via Service Accounts
   - Injeção de segredos via sidecar (Vault Agent)
   - Namespace isolation

4. Recursos Avançados:
   - Geração dinâmica de credenciais (banco de dados, AWS, etc.)
   - Criptografia como serviço (Transit Engine)
   - PKI Management
   - Certificado automático

Tipos de Segredos Gerenciados:

1. Credenciais de Banco de Dados:
   - Usuário/senha para PostgreSQL
   - String de conexão JDBC

2. Chaves de API:
   - Gateways de pagamento (Stripe, PayPal)
   - Serviços de antifraude
   - Provedores de SMS/email

3. Segredos de Aplicação:
   - Chaves JWT
   - Senhas mestras
   - Chaves de criptografia

4. Certificados TLS:
   - Certificados para serviços internos
   - Cadeias de confiança

5. Credenciais de Infraestrutura:
   - Acesso a cloud providers
   - Chaves SSH
   - Tokens de registro Docker

Exemplo de Uso no Código:

@Service
public class DatabaseConfig {

    private final VaultConfig vaultConfig;
    
    public String getDbPassword() {
        return vaultConfig.getSecret("secret/data/pagamento/db")
                         .get("password");
    }
}

Para ambientes de produção, recomenda-se:

1. Configurar TLS com certificados válidos
2. Habilitar o Kubernetes Auth Method
3. Implementar auto-unseal (ex: AWS KMS)
4. Configurar backup automatizado do armazenamento
5. Habilitar HSM para proteção de chaves mestras

Esta implementação transforma o Vault no sistema nervoso central de segurança, garantindo que todos os segredos do sistema de pagamentos sejam gerenciados de forma segura e auditável.
'''
# passo_22_impl_gerenciador_segredos.py
'''
Os arquivos serão gerados na estrutura:

    infra/
    └── vault/
        ├── vault-init.sh
        ├── vault-policy.hcl
        ├── vault-config.hcl
        └── deploy-vault-k8s.yaml
    common/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── pagamento/
                        └── common/
                            └── vault/
                                └── VaultConfig.java

Componentes principais:

1. vault-init.sh
    Script de inicialização do Vault
    Gera chaves de desbloqueio e token root
    Configura secrets engine (KV v2)
    Habilita autenticação AppRole
    Cria política de acesso específica
    Gera credenciais iniciais (role_id e secret_id)
    Habilita auditoria

2. vault-policy.hcl

    Política de acesso para serviços de pagamento
    Permissões específicas para caminhos:
        secret/pagamento/*: Acesso completo
        secret/infra/*: Acesso de leitura
        auth/approle/login: Permissão para login

    Controle de leases e capacidades

3. vault-config.hcl

    Configuração do servidor Vault
    Armazenamento RAFT para alta disponibilidade
    Listener TCP na porta 8200
    Endereços de API e cluster
    UI habilitada para gestão

4. deploy-vault-k8s.yaml

    Kubernetes deployment:

        StatefulSet com volume persistente
        Service para descoberta
        ConfigMaps para configuração
        PersistentVolumeClaim
        RBAC para autenticação Kubernetes

    Configuração completa para implantação

5. VaultConfig.java

    Classe de integração Spring Vault
    Configura VaultTemplate com autenticação AppRole
    Métodos para ler e escrever segredos
    Exemplo de integração com aplicação

Fluxo de implantação:

    Implantar Vault no Kubernetes:
    bash

kubectl apply -f infra/vault/deploy-vault-k8s.yaml

Inicializar e configurar o Vault:
bash

kubectl exec -it vault-0 -n pagamento -- /bin/sh /vault/config/vault-init.sh

Obter credenciais de acesso:
bash

kubectl exec -it vault-0 -n pagamento -- cat /vault/config/credentials.env

Armazenar segredos (exemplo):
bash

    vault kv put secret/pagamento/db username=admin password=secret
    vault kv put secret/pagamento/jwt secretKey=super_secret_123

    Integrar com microsserviços:

        Configurar variáveis:
            VAULT_URI: http://vault.pagamento.svc.cluster.local:8200
            VAULT_ROLE_ID: do credentials.env
            VAULT_SECRET_ID: do credentials.env
        Usar VaultTemplate para acessar segredos

Benefícios:

    Segurança Fortalecida:

        Segredos criptografados em repouso e trânsito
        Rotação automática de credenciais
        Acesso baseado em políticas granulares
        Auditoria completa de acesso

    Gestão Centralizada:

        Todos os segredos em um único lugar
        Versionamento de segredos
        Revogação de acesso instantânea
        Backup e recuperação simplificados

    Integração Nativa:

        Kubernetes Service Account authentication
        Spring Vault para aplicações Java
        Injeção de segredos via sidecar (Vault Agent)

    Recursos Avançados:

        Geração dinâmica de credenciais (banco de dados, cloud, etc.)
        Criptografia como serviço (Transit Engine)
        Gestão de PKI
        Namespace isolation

Esta implementação transforma o Vault no sistema nervoso central
 de segurança, garantindo que todos os segredos do sistema de 
 pagamentos sejam gerenciados de forma segura, auditável e em 
 conformidade com os mais altos padrões de segurança.
