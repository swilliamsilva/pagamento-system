package com.pagamento.common.messaging;

/**
 * @apiNote Classe que representa um tópico Kafka e sua partição específica
 * @author William Silva
 * @contact williamsilva.codigo@gmail.com
 * @technologies Java 8, Spring 2.7 e Maven - Junho de 2025
 * 
 * @openapi
 * 
 * ## Representação de Tópico e Partição
 * 
 * ### Entrada:
 * - Nome do tópico (String)
 * - Número da partição (Integer)
 * 
 * ### Processamento:
 * - 1. Valida os parâmetros de entrada
 * - 2. Armazena a associação tópico/partição
 * - 3. Fornece métodos de acesso
 * 
 * ### Saída:
 * - Objeto imutável representando a combinação tópico-partição
 * 
 * ### Classes Envolvidas:
 * - org.apache.kafka.common.TopicPartition (padrão Kafka)
 * - com.pagamento.common.messaging.ProducerConfig
 * - com.pagamento.common.messaging.SendResult
 * 
 * @example
 * // Criando uma nova instância
 * TopicPartition partition = new TopicPartition("transacoes", 0);
 * 
 * // Recuperando informações
 * String topic = partition.getTopic();
 * int partitionNumber = partition.getPartition();
 */
public class TopicPartition {

    private final String topic;
    private final int partition;

    /**
     * @apiNote Construtor que cria uma associação tópico-partição
     * @param topic Nome do tópico Kafka (não pode ser nulo)
     * @param partition Número da partição (deve ser não-negativo)
     * @throws IllegalArgumentException Se os parâmetros forem inválidos
     * 
     * @openapi
     * 
     * ### Validações realizadas:
     * 1. Tópico não pode ser nulo
     * 2. Tópico não pode ser vazio
     * 3. Partição deve ser número não-negativo
     */
    public TopicPartition(String topic, int partition) {
        if (topic == null || topic.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be null or empty");
        }
        if (partition < 0) {
            throw new IllegalArgumentException("Partition number cannot be negative");
        }
        this.topic = topic;
        this.partition = partition;
    }

    /**
     * @apiNote Retorna o nome do tópico
     * @return Nome do tópico Kafka
     */
    public String getTopic() {
        return topic;
    }

    /**
     * @apiNote Retorna o número da partição
     * @return Número da partição
     */
    public int getPartition() {
        return partition;
    }

    /**
     * @apiNote Compara objetos TopicPartition por valor
     * @param obj Objeto a ser comparado
     * @return true se tópico e partição forem iguais
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TopicPartition that = (TopicPartition) obj;
        return partition == that.partition && topic.equals(that.topic);
    }

    /**
     * @apiNote Gera hash code baseado em tópico e partição
     * @return Hash code do objeto
     */
    @Override
    public int hashCode() {
        return 31 * topic.hashCode() + partition;
    }

    /**
     * @apiNote Representação textual do objeto
     * @return String no formato "tópico-partição"
     */
    @Override
    public String toString() {
        return topic + "-" + partition;
    }
}