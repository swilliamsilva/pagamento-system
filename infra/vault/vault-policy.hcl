# Política de acesso para o serviço de pagamento
path "secret/data/pagamento/*" {
  capabilities = ["create", "read", "update", "delete", "list"]
}

path "secret/metadata/pagamento/*" {
  capabilities = ["list"]
}

path "secret/data/infra/*" {
  capabilities = ["read"]
}

path "auth/approle/login" {
  capabilities = ["create", "read"]
}

path "sys/leases/renew" {
  capabilities = ["create"]
}

path "sys/capabilities-self" {
  capabilities = ["read"]
}
