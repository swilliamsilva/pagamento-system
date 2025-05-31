
# Configuração de CI/CD com GitHub Actions

## Secrets Necessários

| Secret Name                | Descrição                                                                 |
|----------------------------|---------------------------------------------------------------------------|
| `DOCKERHUB_USERNAME`       | Seu nome de usuário do Docker Hub                                         |
| `DOCKERHUB_TOKEN`          | Token de acesso do Docker Hub (Account Settings > Security)               |
| `SONAR_TOKEN`              | Token do SonarCloud (User > Security)                                     |
| `STAGING_KUBECONFIG`       | Kubeconfig para o cluster de staging (codificado em base64)               |
| `PRODUCTION_KUBECONFIG`    | Kubeconfig para o cluster de produção (codificado em base64)              |

## Como Configurar:

1. Acesse o repositório no GitHub
2. Vá em Settings > Secrets and variables > Actions
3. Clique em "New repository secret" para cada segredo

## Gerar Kubeconfig em Base64:

```bash
cat ~/.kube/config | base64
