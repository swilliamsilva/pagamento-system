/* ========================================================
# Classe: ConsumerConfig
# Módulo: Messaging System - Kafka Consumer Configuration
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Kafka 2.8 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Classe de constantes para configuração do consumidor Kafka.
 * 
 * <p>Fluxo de configuração:</p>
 * <ol>
 *   <li><b>Entrada:</b> Propriedades são definidas no application.yml ou programaticamente</li>
 *   <li><b>Processamento:</b> Utilizadas pelo ConsumerFactory para criar consumidores</li>
 *   <li><b>Saída:</b> Configuração aplicada aos consumidores Kafka</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaConsumerFactory - Cria consumidores com estas configurações</li>
 *   <li>KafkaListenerContainerFactory - Configura containers de listener</li>
 *   <li>Consumer - Implementação do consumidor que utiliza estas configurações</li>
 * </ul>
 */
@Schema(description = "Constantes de configuração para consumidores Kafka")
public class ConsumerConfig {

    /**
     * ID do grupo de consumidores.
     * 
     * @Schema(description = "Identificador do grupo de consumidores",
     *         example = "\"pagamento-service-group\"",
     *         defaultValue = "\"${spring.kafka.consumer.group-id}\"")
     */
    public static final String GROUP_ID_CONFIG = "group.id";

    /**
     * Lista de servidores Kafka bootstrap.
     * 
     * @Schema(description = "Lista de servidores Kafka (host:port)",
     *         example = "\"localhost:9092,broker:9093\"",
     *         defaultValue = "\"${spring.kafka.bootstrap-servers}\"")
     */
    public static final String BOOTSTRAP_SERVERS_CONFIG = "bootstrap.servers";

    /**
     * Classe deserializadora para chaves.
     * 
     * @Schema(description = "Deserializador para chaves das mensagens",
     *         implementation = StringDeserializer.class,
     *         defaultValue = "StringDeserializer")
     */
    public static final String KEY_DESERIALIZER_CLASS_CONFIG = "key.deserializer";

    /**
     * Classe deserializadora para valores.
     * 
     * @Schema(description = "Deserializador para valores das mensagens",
     *         implementation = StringDeserializer.class,
     *         defaultValue = "StringDeserializer")
     */
    public static final String VALUE_DESERIALIZER_CLASS_CONFIG = "value.deserializer";

    /**
     * Habilita commit automático de offsets.
     * 
     * @Schema(description = "Se o consumidor deve comitar offsets automaticamente",
     *         example = "false",
     *         defaultValue = "false")
     */
    public static final String ENABLE_AUTO_COMMIT_CONFIG = "enable.auto.commit";

    /**
     * Política de reset quando não há offset inicial.
     * 
     * @Schema(description = "Comportamento quando não há offset inicial",
     *         example = "\"earliest\"",
     *         allowableValues = {"earliest", "latest", "none"},
     *         defaultValue = "\"latest\"")
     */
    public static final String AUTO_OFFSET_RESET_CONFIG = "auto.offset.reset";

    /**
     * Nível de isolamento das leituras.
     * 
     * @Schema(description = "Nível de isolamento para leitura de mensagens",
     *         example = "\"read_committed\"",
     *         allowableValues = {"read_uncommitted", "read_committed"},
     *         defaultValue = "\"read_uncommitted\"")
     */
    public static final String ISOLATION_LEVEL_CONFIG = "isolation.level";

    /**
     * Método auxiliar para criar configuração padrão.
     * 
     * @return Configuração padrão como Properties
     * 
     * @Schema(description = "Cria configuração padrão para consumidores",
     *         example = "{\"bootstrap.servers\":\"localhost:9092\"}")
     */
    public static java.util.Properties createDefaultConfig() {
        java.util.Properties props = new java.util.Properties();
        props.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(GROUP_ID_CONFIG, "pagamento-group");
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }
}