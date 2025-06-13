package com.pagamento.common.messaging;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.common.serialization.Deserializer;

import io.vavr.collection.Map;

/**
 * @apiNote Deserializador para converter bytes recebidos do Kafka em Strings
 * @author William Silva
 * @contact williamsilva.codigo@gmail.com
 * @technologies Java 8, Spring 2.7 e Maven - Junho de 2025
 * 
 * @openapi
 * 
 * ## Fluxo de Desserialização
 * 
 * ### Entrada:
 * - Bytes recebidos do tópico Kafka
 * - Nome do tópico (opcional para configuração)
 * 
 * ### Processamento:
 * - 1. Recebe array de bytes do consumidor Kafka
 * - 2. Converte os bytes para String usando UTF-8
 * - 3. Trata casos de valores nulos
 * 
 * ### Saída:
 * - String desserializada pronta para uso
 * - null se a entrada for null
 * 
 * ### Classes Envolvidas:
 * - org.apache.kafka.common.serialization.Deserializer
 * - org.apache.kafka.common.serialization.StringSerializer
 * 
 * @example
 * // Configuração do consumidor
 * props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 * props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
 */
public class StringDeserializer implements Deserializer<String> {

    /**
     * @apiNote Configura o desserializador (não utilizado nesta implementação)
     * @param configs Configurações (não utilizado)
     * @param isKey Indica se é para chave (não utilizado)
     */
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nenhuma configuração necessária
    }

    /**
     * @apiNote Desserializa bytes para String
     * @param topic Tópico de origem dos dados (não utilizado)
     * @param data Bytes a serem desserializados
     * @return String desserializada ou null se entrada for null
     * 
     * @openapi
     * 
     * ### Fluxo do Método deserialize:
     * 1. Verifica se data é null
     * 2. Converte bytes para String usando UTF-8
     * 3. Retorna resultado
     */
    @Override
    public String deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        return new String(data, StandardCharsets.UTF_8);
    }

    /**
     * @apiNote Fecha o desserializador (não utilizado nesta implementação)
     */
    @Override
    public void close() {
        // Nenhum recurso para liberar
    }

    /**
     * @apiNote Método utilitário para desserialização estática
     * @param data Bytes a serem desserializados
     * @return String desserializada ou null
     */
    public static String deserialize(byte[] data) {
        return new StringDeserializer().deserialize(null, data);
    }
}