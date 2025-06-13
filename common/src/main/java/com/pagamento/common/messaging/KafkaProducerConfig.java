package com.pagamento.common.messaging;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Schema(description = "Configuração do produtor Kafka para o sistema de pagamentos")
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        
        // Configurações básicas
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        // Configurações de confiabilidade
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // Garante escrita em todas as réplicas
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // Tentativas de retry
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Previne duplicações
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1); // Ordem garantida
        
        // Configurações de performance
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip"); // Compactação
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5); // Espera para batch
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16_384); // 16KB
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33_554_432); // 32MB
        
        // Configurações de segurança JSON
        configProps.put("spring.json.trusted.packages", "com.pagamento.dto,com.pagamento.common.dto");
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false); // Previne vulnerabilidades
        
        // Timeouts
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 30_000); // 30 segundos
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 15_000); // 15 segundos

        DefaultKafkaProducerFactory<String, Object> factory = 
            new DefaultKafkaProducerFactory<>(configProps);
        factory.setTransactionIdPrefix("tx-");
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
    
    @Bean
    public org.springframework.kafka.transaction.KafkaTransactionManager<String, Object> kafkaTransactionManager(
            ProducerFactory<String, Object> producerFactory) {
        return new org.springframework.kafka.transaction.KafkaTransactionManager<>(producerFactory);
    }
}
