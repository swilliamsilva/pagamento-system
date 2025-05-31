#!/bin/bash
# Script de inicialização do Vault para ambiente de pagamento
set -e

# Inicializar o Vault
if [ ! -f "/vault/data/vault-init.json" ]; then
    echo "Inicializando o Vault..."
    vault operator init -key-shares=5 -key-threshold=3 > /vault/data/vault-init.json
fi

# Desbloquear o Vault
if [ -f "/vault/data/vault-init.json" ]; then
    echo "Desbloqueando o Vault..."
    UNSEAL_KEYS=$(grep "Unseal Key" /vault/data/vault-init.json | awk '{print $4}')
    counter=1
    for key in $UNSEAL_KEYS; do
        vault operator unseal $key
        counter=$((counter + 1))
        if [ $counter -gt 3 ]; then
            break
        fi
    done
    
    # Configurar token root
    ROOT_TOKEN=$(grep "Initial Root Token" /vault/data/vault-init.json | awk '{print $4}')
    export VAULT_TOKEN=$ROOT_TOKEN
    
    # Habilitar o secrets engine KV v2
    echo "Habilitando KV v2..."
    vault secrets enable -path=secret kv-v2
    
    # Habilitar autenticação AppRole
    echo "Configurando AppRole..."
    vault auth enable approle
    
    # Criar política para o serviço de pagamento
    echo "Configurando políticas..."
    vault policy write pagamento-policy /vault/config/policies/pagamento-policy.hcl
    
    # Configurar AppRole para o serviço de pagamento
    vault write auth/approle/role/pagamento-role         token_policies="pagamento-policy"         token_ttl=1h         token_max_ttl=4h         secret_id_ttl=10m
    
    # Gerar Role ID e Secret ID
    ROLE_ID=$(vault read -field=role_id auth/approle/role/pagamento-role/role-id)
    SECRET_ID=$(vault write -f -field=secret_id auth/approle/role/pagamento-role/secret-id)
    
    # Salvar credenciais iniciais
    echo "VAULT_ROLE_ID=$ROLE_ID" > /vault/config/credentials.env
    echo "VAULT_SECRET_ID=$SECRET_ID" >> /vault/config/credentials.env
    
    # Habilitar auditoria
    vault audit enable file file_path=/vault/logs/audit.log
    
    echo "Configuração inicial do Vault completada!"
else
    echo "Arquivo de inicialização não encontrado!"
    exit 1
fi
