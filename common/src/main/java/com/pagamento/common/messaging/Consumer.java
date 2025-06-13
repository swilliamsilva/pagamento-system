/* ========================================================
# Classe: Consumer
# Módulo: Messaging System - Message Consumer
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Kafka 2.8 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

/**
 * Classe abstrata para consumo de mensagens de sistemas de mensageria.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Recebe mensagens do broker (Kafka, RabbitMQ, etc.)</li>
 *   <li><b>Processamento:</b> Executa lógica de negócio específica para cada mensagem</li>
 *   <li><b>Saída:</b> Confirmação (ACK) ou reprocessamento (NACK) da mensagem</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>MessageListenerContainer - Gerenciador do ciclo de vida do consumer</li>
 *   <li>Acknowledgment - Controle de confirmação de mensagens</li>
 *   <li>ConsumerFactory - Factory para criação de instâncias do consumer</li>
 * </ul>
 * 
 * @param <K> Tipo da chave da mensagem
 * @param <V> Tipo do valor da mensagem
 */
@Schema(description = "Consumidor base para processamento de mensagens")
public abstract class Consumer<K, V> {

    /**
     * Processa uma mensagem recebida.
     * 
     * @param record Registro da mensagem contendo chave, valor e metadados
     * @param acknowledgment Controle de confirmação da mensagem
     * 
     * @Schema(description = "Método principal para processamento de mensagens",
     *         example = "{\"record\":{\"key\":\"123\", \"value\":\"payload\"}, \"acknowledgment\":{}}")
     */
    public abstract void consume(ConsumerRecord<K, V> record, Acknowledgment acknowledgment);

    /**
     * Configurações iniciais do consumer.
     * 
     * @Schema(description = "Callback para configurações personalizadas",
     *         example = "{\"autoStartup\":true, \"concurrency\":3}")
     */
    public void configure() {
        // Configurações padrão podem ser sobrescritas
    }

    /**
     * Tratamento de erros durante o consumo.
     * 
     * @param record Mensagem que causou o erro (pode ser null)
     * @param exception Exceção ocorrida
     * 
     * @Schema(description = "Tratamento personalizado de erros",
     *         example = "{\"record\":{\"key\":\"123\"}, \"exception\":\"RuntimeException\"}")
     */
    public void onError(ConsumerRecord<K, V> record, Exception exception) {
        // Implementação padrão pode ser sobrescrita
    }

    /**
     * Verifica se o consumer está ativo.
     * 
     * @return Status de atividade
     * 
     * @Schema(description = "Verificação de saúde do consumer",
     *         example = "true",
     *         defaultValue = "true")
     */
    public boolean isHealthy() {
        return true;
    }
}