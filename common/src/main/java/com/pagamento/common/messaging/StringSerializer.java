package com.pagamento.common.messaging;

import org.apache.kafka.common.serialization.Serializer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @apiNote Serializador para converter Strings em bytes para envio ao Kafka
 * @author William Silva
 * @contact williamsilva.codigo@gmail.com
 * @technologies Java 8, Spring 2.7 e Maven - Junho de 2025
 * 
 * @openapi
 * 
 * ## Fluxo de Serialização
 * 
 * ### Entrada:
 * - String a ser serializada
 * - Nome do tópico (opcional para configuração)
 * 
 * ### Processamento:
 * - 1. Recebe a String do produtor Kafka
 * - 2. Converte a String para bytes usando UTF-8
 * - 3. Trata casos de valores nulos
 * 
 * ### Saída:
 * - Bytes serializados prontos para envio ao Kafka
 * - null se a entrada for null
 * 
 * ### Classes Envolvidas:
 * - org.apache.kafka.common.serialization.Serializer
 * - org.apache.kafka.common.serialization.StringDeserializer
 * - com.pagamento.common.messaging.ProducerConfig
 * 
 * @example
 * // Configuração do produtor
 * props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
 * props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
 */
public class StringSerializer implements Serializer<String> {

    /**
     * @apiNote Configura o serializador (não utilizado nesta implementação)
     * @param configs Configurações (não utilizado)
     * @param isKey Indica se é para chave (não utilizado)
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // Nenhuma configuração necessária
    }

    /**
     * @apiNote Serializa String para bytes
     * @param topic Tópico de destino dos dados (não utilizado)
     * @param data String a ser serializada
     * @return Bytes serializados ou null se entrada for null
     * 
     * @openapi
     * 
     * ### Fluxo do Método serialize:
     * 1. Verifica se data é null
     * 2. Converte String para bytes usando UTF-8
     * 3. Retorna resultado
     */
    @Override
    public byte[] serialize(String topic, String data) {
        if (data == null) {
            return null;
        }
        return data.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * @apiNote Fecha o serializador (não utilizado nesta implementação)
     */
    @Override
    public void close() {
        // Nenhum recurso para liberar
    }

    /**
     * @apiNote Método utilitário para serialização estática
     * @param data String a ser serializada
     * @return Bytes serializados ou null
     */
    public static byte[] serialize(String data) {
        return new StringSerializer().serialize(null, data);
    }
}