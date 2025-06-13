package com.pagamento.common.messaging;

/**
 * @apiNote Classe de configuração do produtor de mensagens Kafka
 * @author William Silva
 * @contact williamsilva.codigo@gmail.com
 * @technologies Java 8, Spring 2.7 e Maven - Junho de 2025
 * 
 * @openapi
 * 
 * ## Fluxo de Configuração do Produtor Kafka
 * 
 * ### Entrada:
 * - Parâmetros de configuração do produtor Kafka
 * - Constantes pré-definidas para configuração do cliente produtor
 * 
 * ### Processamento:
 * - 1. Definição das constantes de configuração padrão
 * - 2. Disponibilização das configurações para criação do KafkaProducer
 * - 3. Configuração de serializadores, políticas de ACK e outros parâmetros
 * 
 * ### Saída:
 * - Configurações prontas para serem utilizadas na criação de um KafkaProducer
 * 
 * ### Classes Envolvidas:
 * - org.apache.kafka.clients.producer.KafkaProducer
 * - org.apache.kafka.common.serialization.Serializer
 * - org.apache.kafka.clients.producer.ProducerConfig
 * 
 * @example
 * Properties props = new Properties();
 * props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
 * props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
 * props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
 * KafkaProducer<String, String> producer = new KafkaProducer<>(props);
 */
public class ProducerConfig {

    /**
     * @apiNote Lista de servidores Kafka bootstrap (host:port)
     * @value null (deve ser configurado)
     */
    public static final String BOOTSTRAP_SERVERS_CONFIG = null;

    /**
     * @apiNote Serializador para chaves das mensagens
     * @value null (deve ser configurado com classe serializadora)
     */
    public static final String KEY_SERIALIZER_CLASS_CONFIG = null;

    /**
     * @apiNote Configuração de confirmação de recebimento (acks)
     * @value null (valores comuns: "all", "1", "0")
     */
    public static final String ACKS_CONFIG = null;

    /**
     * @apiNote Serializador para valores das mensagens
     * @value null (deve ser configurado com classe serializadora)
     */
    public static final String VALUE_SERIALIZER_CLASS_CONFIG = null;

    /**
     * @apiNote Número de tentativas de reenvio em caso de falha
     * @value null (valor padrão recomendado: 3)
     */
    public static final String RETRIES_CONFIG = null;

    /**
     * @apiNote Habilita modo idempotente (evita duplicação)
     * @value null (valores: "true" ou "false")
     */
    public static final String ENABLE_IDEMPOTENCE_CONFIG = null;

    /**
     * @apiNote Máximo de requisições não respondidas por conexão
     * @value null (valor padrão recomendado: 5)
     */
    public static final String MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = null;

    /**
     * @apiNote Tipo de compressão das mensagens
     * @value null (valores: "none", "gzip", "snappy", "lz4", "zstd")
     */
    public static final String COMPRESSION_TYPE_CONFIG = null;

    // Nota: Removi as constantes duplicadas (KEY_SERIALIZER_CLASS_CONFIG11 e KEY_SERIALIZER_CLASS_CONFIG1)
    // pois parecem ser erros de digitação
}