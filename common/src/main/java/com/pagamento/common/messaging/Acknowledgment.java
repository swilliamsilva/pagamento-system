/* ========================================================
# Classe: Acknowledgment
# Módulo: Messaging System - Message Acknowledgment
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Classe responsável pelo controle de acknowledgment (confirmação de recebimento) de mensagens.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Recebe solicitação de confirmação de processamento de mensagem</li>
 *   <li><b>Processamento:</b> Envia confirmação para o broker de mensagens</li>
 *   <li><b>Saída:</b> Mensagem é marcada como processada no sistema de mensageria</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>MessageListener - Classe que recebe a mensagem original</li>
 *   <li>RabbitTemplate/JmsTemplate - Classes de template para envio de confirmação</li>
 *   <li>MessageBroker - Broker de mensagens (RabbitMQ, ActiveMQ, etc)</li>
 * </ul>
 */
@Schema(description = "Controle de confirmação de recebimento/processamento de mensagens")
public class Acknowledgment {

    /**
     * Confirma o processamento bem-sucedido de uma mensagem.
     * 
     * <p>Quando chamado:</p>
     * <ul>
     *   <li>Remove a mensagem da fila do broker</li>
     *   <li>Indica que o processamento foi concluído com sucesso</li>
     *   <li>Deve ser chamado após todo o processamento da mensagem</li>
     * </ul>
     * 
     * @Schema(description = "Confirma o processamento de uma mensagem",
     *         example = "{\"operation\":\"acknowledge\"}")
     */
    public void acknowledge() {
        // Implementação real enviaria ACK para o broker
        // brokerConnection.sendAck();
    }

    /**
     * Método para negar o processamento (negative acknowledgment).
     * 
     * @param requeue Indica se a mensagem deve ser recolocada na fila
     * 
     * @Schema(description = "Rejeita uma mensagem, podendo recolocá-la na fila",
     *         example = "{\"requeue\":false}",
     *         defaultValue = "false")
     */
    public void reject(boolean requeue) {
        // Implementação real enviaria NACK para o broker
        // brokerConnection.sendNack(requeue);
    }
}