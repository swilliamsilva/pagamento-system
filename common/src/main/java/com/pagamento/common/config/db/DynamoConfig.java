package com.pagamento.common.config.db;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.pagamento.pix.repository.dynamo")
public class DynamoConfig {
    // Configuração do DynamoDB
}
