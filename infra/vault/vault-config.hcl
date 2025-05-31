# Configuração do servidor Vault
storage "raft" {
  path    = "/vault/data"
  node_id = "node1"
}

listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = 1  # Em produção, usar certificados reais
}

api_addr = "http://vault.pagamento.svc.cluster.local:8200"
cluster_addr = "https://vault.pagamento.svc.cluster.local:8201"
ui = true
