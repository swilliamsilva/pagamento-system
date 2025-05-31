Arquitetura de Deployment:

1. Docker Compose (Ambiente de Desenvolvimento):
   - Serviços: PostgreSQL, Kafka, Eureka, Auth, Payment, Gateway
   - Rede isolada: pagamento-net
   - Health checks automatizados
   - Volumes persistente para PostgreSQL

2. Kubernetes (Ambiente de Produção):
   - Despliegues escaláveis com múltiplas réplicas
   - Configuração centralizada com ConfigMaps e Secrets
   - Service Discovery com Eureka
   - StatefulSets para Kafka e PostgreSQL
   - Ingress com TLS e regras de roteamento

Componentes Principais:

A. Docker:
   - Dockerfile otimizados para cada serviço
   - Imagens base leves (eclipse-temurin:17-jdk-alpine)
   - Configuração de JVM para desempenho
   - Health checks para monitoramento

B. Kubernetes:
   - Auth Service: 3 réplicas com limites de recursos
   - Gateway Service: 2 réplicas com LoadBalancer
   - Service Discovery: Eureka para registro de serviços
   - ConfigMap: Configurações comuns para todos os serviços
   - Secrets: Dados sensíveis codificados em base64
   - Ingress: Roteamento inteligente com TLS

C. Infraestrutura:
   - Kafka: StatefulSet com 3 brokers para alta disponibilidade
   - PostgreSQL: StatefulSet com volume persistente
   - Eureka: Service discovery para microserviços

Fluxo de Implantação:

1. Construir imagens:
   docker build -f deployment/docker/Dockerfile-auth -t pagamento/auth-service:1.0.0 .
   docker build -f deployment/docker/Dockerfile-payment -t pagamento/payment-service:1.0.0 .
   docker build -f deployment/docker/Dockerfile-gateway -t pagamento/gateway-service:1.0.0 .

2. Executar localmente:
   docker-compose -f deployment/docker/docker-compose.yml up -d

3. Implantar no Kubernetes:
   kubectl apply -f deployment/k8s/configmap.yaml
   kubectl apply -f deployment/k8s/secrets.yaml
   kubectl apply -f deployment/k8s/service-discovery.yaml
   kubectl apply -f deployment/k8s/kafka.yaml
   kubectl apply -f deployment/k8s/postgres.yaml
   kubectl apply -f deployment/k8s/auth-deployment.yaml
   kubectl apply -f deployment/k8s/gateway-deployment.yaml
   kubectl apply -f deployment/k8s/ingress.yaml

Boas Práticas Implementadas:

1. Segurança:
   - Secrets codificados em base64
   - TLS obrigatório no Ingress
   - Limites de recursos para containers
   - Probes para health checks

2. Escalabilidade:
   - Múltiplas réplicas para serviços stateless
   - StatefulSets para serviços stateful
   - HPA (Horizontal Pod Autoscaler) pronto para uso

3. Resilência:
   - Rolling updates com maxSurge/maxUnavailable
   - Probes de readiness e liveness
   - Tolerâncias a falhas configuradas

4. Observabilidade:
   - Portas expostas para scraping do Prometheus
   - Endpoints Actuator habilitados
   - Logs estruturados configurados

5. Armazenamento:
   - Volumes persistentes para Kafka e PostgreSQL
   - Claims de volume configurados

Configurações Específicas de Ambiente:

- Desenvolvimento (Docker Compose):
  SPRING_PROFILES_ACTIVE: docker
  Comunicação direta entre containers

- Produção (Kubernetes):
  SPRING_PROFILES_ACTIVE: k8s
  Service discovery via Eureka
  Configurações via ConfigMap/Secrets
  Roteamento via Ingress

Recomendações para Produção:

1. Usar um registry de imagens privado
2. Implementar CI/CD para builds automatizados
3. Configurar HPA (Horizontal Pod Autoscaler)
4. Usar Network Policies para segurança
5. Implementar backup automatizado para volumes
6. Configurar autenticação no Kubernetes
7. Usar service mesh (Istio/Linkerd) para observabilidade avançada

Esta implementação fornece uma base completa para implantar o sistema de pagamentos em qualquer ambiente, desde uma máquina local até clusters Kubernetes de produção em nuvem.
'''
# passo_20_impl_docker_kubernets.py
'''
Os arquivos serão gerados nas estruturas:

    deployment/
    ├── docker/
    │   ├── Dockerfile-auth
    │   ├── Dockerfile-payment
    │   ├── Dockerfile-gateway
    │   └── docker-compose.yml
    └── k8s/
        ├── auth-deployment.yaml
        ├── gateway-deployment.yaml
        ├── service-discovery.yaml
        ├── configmap.yaml
        ├── secrets.yaml
        ├── kafka.yaml
        ├── postgres.yaml
        └── ingress.yaml

Componentes principais:

Docker Compose (desenvolvimento local):
    PostgreSQL: Banco de dados principal
    Kafka + Zookeeper: Mensageria assíncrona
    Eureka: Service discovery
    Auth Service: Autenticação de usuários
    Payment Service: Processamento de pagamentos
    Gateway Service: API Gateway

Kubernetes (produção):
    StatefulSets:
        Kafka (3 brokers)
        PostgreSQL (com volume persistente)

    Deployments:
        Auth Service (3 réplicas)
        Payment Service (3 réplicas)
        Gateway Service (2 réplicas)
        Service Discovery (Eureka)

    Configuração:

        ConfigMap: Variáveis de ambiente comuns
        Secrets: Dados sensíveis (base64)
        Ingress: Roteamento com TLS

    Serviços:

        ClusterIP para serviços internos
        LoadBalancer para Gateway
        Headless Services para StatefulSets

Características avançadas:

Docker:

    Imagens otimizadas com Alpine Linux
    Health checks automatizados
    Configuração de JVM para desempenho
    Volumes persistente para PostgreSQL
    Rede isolada entre serviços

Kubernetes:

    Rolling updates com controle de disponibilidade
    Resource limits e requests
    Readiness e liveness probes
    Service discovery integrado
    TLS automático com cert-manager
    Ingress com regras de roteamento
    Annotations para Prometheus scraping

Fluxo de implantação:

    Construir imagens Docker:
    bash

docker build -f deployment/docker/Dockerfile-auth -t pagamento/auth-service:1.0.0 .
docker build -f deployment/docker/Dockerfile-payment -t pagamento/payment-service:1.0.0 .
docker build -f deployment/docker/Dockerfile-gateway -t pagamento/gateway-service:1.0.0 .

Executar localmente:
bash

docker-compose -f deployment/docker/docker-compose.yml up -d

Implantar no Kubernetes:
bash

    kubectl apply -f deployment/k8s/configmap.yaml
    kubectl apply -f deployment/k8s/secrets.yaml
    kubectl apply -f deployment/k8s/service-discovery.yaml
    kubectl apply -f deployment/k8s/kafka.yaml
    kubectl apply -f deployment/k8s/postgres.yaml
    kubectl apply -f deployment/k8s/auth-deployment.yaml
    kubectl apply -f deployment/k8s/gateway-deployment.yaml
    kubectl apply -f deployment/k8s/ingress.yaml

Benefícios:

    Portabilidade: Executa em qualquer ambiente Docker/K8s
    Escalabilidade: Fácil escalonamento horizontal
    Resiliência: Tolerância a falhas incorporada
    Segurança: Configuração TLS e gestão de segredos
    Observabilidade: Integração com ferramentas de monitoramento
    DevOps Amigável: CI/CD simplificada

Esta implementação fornece uma infraestrutura profissional 
pronta para produção, seguindo as melhores práticas de 
containerização e orquestração para sistemas de pagamentos de 
alta disponibilidade.
