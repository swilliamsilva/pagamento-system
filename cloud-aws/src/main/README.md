Arquitetura de Integração com AWS:

1. Configuração de Clientes:
   - S3ClientConfig: Configura cliente S3 e S3Presigner
   - SnsClientConfig: Configura cliente SNS
   - Suporte a endpoints customizados (LocalStack)
   - Injeção de credenciais centralizada

2. Serviços AWS:
   - S3Service:
     • Upload/download de arquivos
     • Geração de URLs pré-assinadas
     • Criação de buckets e políticas
     • Gerenciamento de metadados
   - SnsService:
     • Publicação de mensagens em tópicos
     • Criação de tópicos e subscrições
     • Atributos de tópicos
     • SMS para números de telefone

3. Gestão de Credenciais:
   - Perfil de produção: Usa DefaultCredentialsProvider (EC2 IAM Roles)
   - Perfil local: Credenciais estáticas para LocalStack
   - Suporte a credenciais explícitas via propriedades

Como Usar:

1. Upload de arquivos para S3:

@Autowired
private S3Service s3Service;

public void uploadInvoice(byte[] pdfContent, String invoiceId) {
    s3Service.uploadFile(
        "pagamento-invoices", 
        "invoices/" + invoiceId + ".pdf",
        pdfContent,
        "application/pdf"
    );
}

2. Geração de URL pré-assinada:

public String getInvoiceUrl(String invoiceId) {
    return s3Service.generatePresignedUrl(
        "pagamento-invoices",
        "invoices/" + invoiceId + ".pdf",
        Duration.ofMinutes(15)
    );
}

3. Publicação de eventos no SNS:

@Autowired
private SnsService snsService;

public void publishPaymentEvent(PaymentEvent event) {
    String message = objectMapper.writeValueAsString(event);
    snsService.publishMessage(
        "arn:aws:sns:us-east-1:123456789012:payment-events",
        message
    );
}

4. Envio de SMS:

public void sendPaymentConfirmationSms(String phone, String transactionId) {
    String message = "Pagamento aprovado! ID: " + transactionId;
    snsService.publishToPhone(phone, message);
}

Configuração no application.yml:

aws:
  region: us-east-1
  # Para LocalStack:
  s3:
    endpoint: http://localhost:4566
  sns:
    endpoint: http://localhost:4566
  # Credenciais explícitas (opcional):
  accessKeyId: AKIAIOSFODNN7EXAMPLE
  secretKey: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY

Benefícios da Implementação:

1. Armazenamento Seguro:
   - Upload de documentos (comprovantes, faturas)
   - Armazenamento de relatórios
   - Backup de dados críticos

2. Notificações em Tempo Real:
   - Eventos de pagamento (aprovado, recusado)
   - Alertas de fraude
   - Notificações para administradores

3. Integração com AWS:
   - S3: Armazenamento durável e escalável
   - SNS: Mensageria pub/sub totalmente gerenciada
   - Políticas de segurança granulares
   - Criptografia em repouso e trânsito

4. Desenvolvimento Local:
   - Compatível com LocalStack
   - Testes isolados sem recursos reais da AWS
   - Configuração simplificada

Casos de Uso Específicos:

1. Comprovantes de Pagamento:
   - Armazenar PDFs no S3
   - Gerar URLs temporários para clientes

2. Eventos de Pagamento:
   - Publicar eventos no SNS para outros serviços
   - Integrar com SQS para processamento assíncrono
   - Disparar notificações por email/SMS

3. Backup de Dados:
   - Backup diário de banco de dados no S3
   - Versionamento de objetos
   - Políticas de lifecycle para arquivamento

4. Logs e Auditoria:
   - Armazenar logs de auditoria no S3
   - Publicar alertas críticos via SNS

Configurações Avançadas:

1. Criptografia:
   - SSE-S3, SSE-KMS para dados no S3
   - HTTPS obrigatório para todas as comunicações

2. Segurança:
   - Bucket policies restritivas
   - IAM roles para acesso mínimo necessário
   - Block Public Access habilitado

3. Monitoramento:
   - CloudWatch para métricas do S3/SNS
   - Logging de acesso ao S3
   - Notificações de eventos do S3

4. Otimização:
   - Transfer Acceleration para S3
   - Compressão de objetos
   - CDN via CloudFront

Esta implementação fornece uma integração robusta e segura com os serviços da AWS, permitindo que o sistema de pagamentos aproveite a escalabilidade e confiabilidade da nuvem.
'''

# passo_23_impl_servicos_AWS.py

'''
Os arquivos serão gerados na estrutura:

    cloud-aws/
    └── src/
        └── main/
            └── java/
                └── com/
                    └── pagamento/
                        └── aws/
                            ├── s3/
                            │   ├── S3ClientConfig.java
                            │   └── S3Service.java
                            ├── sns/
                            │   ├── SnsClientConfig.java
                            │   └── SnsService.java
                            └── AwsCredentialsConfig.java

Componentes principais:

1. S3ClientConfig:
    Configura o cliente S3 e S3Presigner
    Suporte a endpoints customizados (para LocalStack)
    Configuração de região AWS
    Injeção de credenciais via AwsCredentialsProvider

2. S3Service:
    uploadFile: Upload de arquivos para o S3
    downloadFile: Download de arquivos do S3
    generatePresignedUrl: Gera URLs temporários para acesso
    createBucket: Cria novos buckets
    setBucketPolicy: Define políticas de acesso
    setObjectMetadata: Gerencia metadados de objetos

3. SnsClientConfig:

    Configura o cliente SNS
    Suporte a endpoints customizados
    Configuração de região AWS
    Injeção de credenciais

4. SnsService:

    publishMessage: Publica mensagens em tópicos SNS
    createTopic: Cria novos tópicos
    subscribeToTopic: Adiciona subscrições
    setTopicAttributes: Configura atributos de tópicos
    publishToPhone: Envia SMS via SNS
    publishWithSubject: Publica com assunto

5. AwsCredentialsConfig:

    Configuração centralizada de credenciais
    Perfil de produção: Usa IAM Roles ou credenciais explícitas
    Perfil de desenvolvimento: Credenciais estáticas para LocalStack
    Compatível com Default Credential Provider Chain

Casos de Uso:

S3 (Armazenamento de Objetos):

    Armazenar comprovantes de pagamento em PDF
    Backup de banco de dados
    Armazenamento de logs de auditoria
    Hospedagem de arquivos estáticos

SNS (Notificações):

    Notificar sistemas sobre eventos de pagamento
    Disparar SMS de confirmação para clientes
    Alertas de fraude em tempo real
    Notificações para equipe de operações

Configuração LocalStack (Desenvolvimento):
yaml

# docker-compose.yml
services:
  localstack:
    image: localstack/localstack
    ports:
      - "4566:4566"
    environment:
      - SERVICES=s3,sns
      - DEFAULT_REGION=us-east-1
      - AWS_ACCESS_KEY_ID=test
      - AWS_SECRET_ACCESS_KEY=test

Configuração application.yml:
yaml

aws:
  region: us-east-1
  # Configurações para LocalStack:
  s3:
    endpoint: http://localhost:4566
  sns:
    endpoint: http://localhost:4566
  # Para produção:
  # accessKeyId: ${AWS_ACCESS_KEY}
  # secretKey: ${AWS_SECRET_KEY}

Benefícios:

    Escalabilidade: Serviços totalmente gerenciados e escaláveis
    Confiabilidade: SLA de 99.9% da AWS
    Segurança: Criptografia, políticas granulares e IAM
    Custo-efetividade: Pagamento somente pelo uso
    Integração: Ecossistema completo de serviços AWS

Esta implementação fornece uma integração robusta e segura com os serviços da AWS, permitindo que o sistema de pagamentos aproveite a escalabilidade e confiabilidade da nuvem para operações críticas.

'''