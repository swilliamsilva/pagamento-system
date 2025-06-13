/* ========================================================
# Classe: ConcurrentKafkaListenerContainerFactory
# Módulo: Messaging System - Kafka Listener Configuration
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Kafka 2.8 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * Factory para criação de containers de listener Kafka concorrentes.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Configurações do consumidor e propriedades do container</li>
 *   <li><b>Processamento:</b> Cria múltiplas instâncias de listener para processamento paralelo</li>
 *   <li><b>Saída:</b> Container configurado pronto para receber mensagens Kafka</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaListener - Anotação que define os listeners</li>
 *   <li>ConsumerFactory - Factory para criação de consumidores Kafka</li>
 *   <li>ConcurrentMessageListenerContainer - Container que gerencia os listeners</li>
 * </ul>
 */
@Schema(description = "Factory para configuração de listeners Kafka concorrentes")
public class ConcurrentKafkaListenerContainerFactory<K, V> {

    private ConsumerFactory<K, V> consumerFactory;
    private ContainerProperties containerProperties = new ContainerProperties("");
    private Integer concurrency = 1;

    /**
     * Configura a fábrica de consumidores Kafka.
     * 
     * @param consumerFactory Fábrica de consumidores
     * 
     * @Schema(description = "Define a fábrica de consumidores Kafka",
     *         example = "{\"consumerFactory\":\"@primaryConsumerFactory\"}")
     */
    public void setConsumerFactory(ConsumerFactory<K, V> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    /**
     * Obtém as propriedades de configuração do container.
     * 
     * @return ContainerProperties Configurações do container
     * 
     * @Schema(description = "Retorna as propriedades de configuração do container",
     *         implementation = ContainerProperties.class)
     */
    public ContainerProperties getContainerProperties() {
        return this.containerProperties;
    }

    /**
     * Define o nível de concorrência (número de listeners paralelos).
     * 
     * @param concurrency Número de threads de listener
     * 
     * @Schema(description = "Define o número de listeners concorrentes",
     *         example = "3",
     *         defaultValue = "1")
     */
    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    /**
     * Cria um novo container de listener configurado.
     * 
     * @param topics Tópicos Kafka para assinar
     * @return Container configurado
     * 
     * @Schema(description = "Cria container de listener para os tópicos especificados",
     *         example = "{\"topics\":[\"pagamentos\"]}")
     */
    public ConcurrentMessageListenerContainer<K, V> createContainer(String... topics) {
        ContainerProperties properties = new ContainerProperties(topics);
        // Configurações adicionais podem ser aplicadas aqui
        return new ConcurrentMessageListenerContainer<>(consumerFactory, properties);
    }

    /**
     * Configura o modo de commit de offsets.
     * 
     * @param commitMode Modo de commit (BATCH, RECORD, etc.)
     * 
     * @Schema(description = "Define o modo de commit de offsets",
     *         example = "\"BATCH\"",
     *         allowableValues = {"BATCH", "RECORD", "TIME", "COUNT", "COUNT_TIME"})
     */
    public void setAckMode(ContainerProperties.AckMode ackMode) {
        this.containerProperties.setAckMode(ackMode);
    }
}