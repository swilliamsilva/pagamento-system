/* ========================================================
# Classe: CustomConsumerFactory
# Módulo: Messaging System - Kafka Consumer Factory
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Kafka 2.8 e Maven - Junho de 2025
# ======================================================== */
package com.pagamento.common.messaging;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.core.ConsumerFactory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Factory para criação de consumidores Kafka com configuração centralizada.
 */
@Schema(description = "Factory para criação de consumidores Kafka configuráveis")
public class CustomConsumerFactory<K, V> implements ConsumerFactory<K, V> {

    private final Map<String, Object> configs;
    private final Class<K> keyType;
    private final Class<V> valueType;

    public CustomConsumerFactory(Map<String, Object> configs, Class<K> keyType, Class<V> valueType) {
        this.configs = new HashMap<>(Objects.requireNonNull(configs));
        this.keyType = Objects.requireNonNull(keyType);
        this.valueType = Objects.requireNonNull(valueType);
    }

    @Override
    public Consumer<K, V> createConsumer() {
        validateRequiredConfigs();
        return new KafkaConsumer<>(this.configs, null, null);
    }

    @Override
    public Consumer<K, V> createConsumer(String groupId, String clientIdPrefix, String clientIdSuffix) {
        Map<String, Object> newConfigs = new HashMap<>(this.configs);
        newConfigs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        
        if (clientIdPrefix != null) {
            String clientId = clientIdPrefix + (clientIdSuffix != null ? "-" + clientIdSuffix : "");
            newConfigs.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
        }
        
        return new KafkaConsumer<>(newConfigs, null, null);
    }

    public Consumer<K, V> createConsumer(String groupId, String clientIdPrefix) {
        return createConsumer(groupId, clientIdPrefix, null);
    }

    @Override
    public boolean isAutoCommit() {
        Object autoCommit = configs.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG);
        return autoCommit instanceof Boolean ? (Boolean) autoCommit : false;
    }

    private void validateRequiredConfigs() {
        if (!configs.containsKey(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG)) {
            throw new IllegalStateException("Bootstrap servers config is required");
        }
    }

    // Getters
    public Class<K> getKeyType() {
        return keyType;
    }

    public Class<V> getValueType() {
        return valueType;
    }

    public Map<String, Object> getConfigs() {
        return new HashMap<>(configs);
    }
}