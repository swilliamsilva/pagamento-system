/* ========================================================
# Classe: ConsumerRecord
# Módulo: Messaging System - Kafka Message Record
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Kafka 2.8 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Optional;

/**
 * Representa um registro de mensagem consumida do Kafka com metadados completos.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Recebido do broker Kafka quando uma nova mensagem está disponível</li>
 *   <li><b>Processamento:</b> Contém todos os metadados e o payload da mensagem</li>
 *   <li><b>Saída:</b> Passado para o método de processamento do consumidor</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaConsumer - Produz estes registros durante o consumo</li>
 *   <li>MessageListener - Processa os registros recebidos</li>
 *   <li>Acknowledgment - Usado para confirmar o processamento</li>
 * </ul>
 * 
 * @param <K> Tipo da chave da mensagem
 * @param <V> Tipo do valor/payload da mensagem
 */
@Schema(description = "Registro completo de uma mensagem Kafka consumida")
public class ConsumerRecord<K, V> {

    private final String topic;
    private final Integer partition;
    private final Long offset;
    private final K key;
    private final V value;
    private final Long timestamp;

    /**
     * Cria um novo registro de mensagem.
     */
    public ConsumerRecord(String topic, Integer partition, Long offset, K key, V value, Long timestamp) {
        this.topic = topic;
        this.partition = partition;
        this.offset = offset;
        this.key = key;
        this.value = value;
        this.timestamp = timestamp;
    }

    /**
     * Obtém o tópico de origem da mensagem.
     * 
     * @return Nome do tópico Kafka
     * 
     * @Schema(description = "Tópico Kafka de origem da mensagem",
     *         example = "\"pagamentos\"",
     *         required = true)
     */
    public String topic() {
        return this.topic;
    }

    /**
     * Obtém o offset da mensagem na partição.
     * 
     * @return Número do offset
     * 
     * @Schema(description = "Posição da mensagem na partição",
     *         example = "42",
     *         required = true)
     */
    public long offset() {
        return this.offset;
    }

    /**
     * Obtém a chave da mensagem.
     * 
     * @return Chave da mensagem (pode ser null)
     * 
     * @Schema(description = "Chave da mensagem para particionamento",
     *         example = "\"order-123\"")
     */
    public Optional<K> key() {
        return Optional.ofNullable(this.key);
    }

    /**
     * Obtém o número da partição.
     * 
     * @return Número da partição
     * 
     * @Schema(description = "Partição do tópico onde a mensagem foi publicada",
     *         example = "3",
     *         required = true)
     */
    public int partition() {
        return this.partition;
    }

    /**
     * Obtém o payload da mensagem.
     * 
     * @return Valor/conteúdo da mensagem
     * 
     * @Schema(description = "Conteúdo principal da mensagem",
     *         example = "\"{\\\"id\\\":123}\"",
     *         required = true)
     */
    public V value() {
        return this.value;
    }

    /**
     * Obtém o timestamp da mensagem.
     * 
     * @return Timestamp de criação/publicação
     * 
     * @Schema(description = "Timestamp da mensagem em milissegundos",
     *         example = "1625097600000")
     */
    public long timestamp() {
        return this.timestamp;
    }

    /**
     * Representação textual do registro.
     * 
     * @return String formatada com metadados principais
     * 
     * @Schema(description = "Representação resumida do registro",
     *         example = "ConsumerRecord(topic=pagamentos, partition=0, offset=42)")
     */
    @Override
    public String toString() {
        return String.format("ConsumerRecord(topic=%s, partition=%d, offset=%d)", 
               topic, partition, offset);
    }
}