package com.pagamento.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;

@Configuration
public class AwsCredentialsConfig {

    @Value("${aws.accessKeyId:}")
    private String accessKey;

    @Value("${aws.secretKey:}")
    private String secretKey;

    @Bean
    @Profile("!dev & !test")
    public AwsCredentialsProvider awsCredentialsProvider() {
        if (!accessKey.isEmpty() && !secretKey.isEmpty()) {
            return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            );
        }
        return DefaultCredentialsProvider.create();
    }

    @Bean
    @Profile("dev | test")
    public AwsCredentialsProvider localAwsCredentialsProvider() {
        // Para desenvolvimento local com LocalStack
        return StaticCredentialsProvider.create(
            AwsBasicCredentials.create("test", "test")
        );
    }
}
