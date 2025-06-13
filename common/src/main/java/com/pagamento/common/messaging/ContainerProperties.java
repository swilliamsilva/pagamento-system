/* ========================================================
# Classe: ContainerProperties
# Módulo: Messaging System - Kafka Listener Container
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Kafka 2.8 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Configurações de propriedades para containers de listener Kafka.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Configurações definidas pelo desenvolvedor</li>
 *   <li><b>Processamento:</b> Aplicadas pelo container de listener</li>
 *   <li><b>Saída:</b> Comportamento personalizado do consumidor Kafka</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>ConcurrentKafkaListenerContainerFactory - Usa estas propriedades</li>
 *   <li>KafkaMessageListenerContainer - Implementa o comportamento</li>
 *   <li>ConsumerFactory - Cria consumidores com estas configurações</li>
 * </ul>
 */
@Schema(description = "Propriedades de configuração para containers de listener Kafka")
public class ContainerProperties {

    /**
     * Modos de confirmação (acknowledgment) disponíveis.
     */
    @Schema(description = "Modos de confirmação de mensagens suportados")
    public enum AckMode {
        @Schema(description = "Confirma após cada mensagem processada")
        RECORD,
        
        @Schema(description = "Confirma em lotes quando todos os registros do poll são processados")
        BATCH,
        
        @Schema(description = "Confirma após intervalo de tempo configurado")
        TIME,
        
        @Schema(description = "Confirma após número de mensagens processadas")
        COUNT,
        
        @Schema(description = "Confirma após intervalo OU número de mensagens, o que ocorrer primeiro")
        COUNT_TIME,
        
        @Schema(description = "Confirmação manual pelo aplicativo")
        MANUAL,
        
        @Schema(description = "Confirmação manual em lotes pelo aplicativo")
        MANUAL_IMMEDIATE
    }

    private AckMode ackMode = AckMode.BATCH;
    private String[] topics;
    private Long ackTime;
    private Integer ackCount;
    private Long pollTimeout = 1000L;

    /**
     * Cria propriedades para os tópicos especificados.
     * 
     * @param topics Tópicos para assinar
     */
    public ContainerProperties(String... topics) {
        this.topics = topics;
    }

    /**
     * Obtém o modo de confirmação atual.
     * 
     * @return Modo de confirmação configurado
     */
    @Schema(description = "Modo de confirmação de offsets", 
            example = "BATCH", 
            defaultValue = "BATCH",
            implementation = AckMode.class)
    public AckMode getAckMode() {
        return ackMode;
    }

    /**
     * Define o modo de confirmação.
     * 
     * @param ackMode Modo de confirmação
     */
    public void setAckMode(AckMode ackMode) {
        this.ackMode = ackMode;
    }

    /**
     * Obtém a lista de tópicos assinados.
     * 
     * @return Array de tópicos
     */
    @Schema(description = "Tópicos assinados pelo container",
            example = "[\"pagamentos\", \"notificacoes\"]")
    public String[] getTopics() {
        return topics;
    }

    /**
     * Define o timeout para operações de poll.
     * 
     * @param pollTimeout Timeout em milissegundos
     */
    @Schema(description = "Timeout para operações de poll em ms",
            example = "5000",
            defaultValue = "1000")
    public void setPollTimeout(Long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    /**
     * Configura parâmetros para modo TIME.
     * 
     * @param ackTime Intervalo em ms
     */
    public void setAckTime(Long ackTime) {
        this.ackTime = ackTime;
    }

    /**
     * Configura parâmetros para modo COUNT.
     * 
     * @param ackCount Número de mensagens
     */
    public void setAckCount(Integer ackCount) {
        this.ackCount = ackCount;
    }

    /**
     * Verifica se o container está configurado para confirmação manual.
     * 
     * @return Verdadeiro se for modo manual
     */
    public boolean isManualAck() {
        return ackMode == AckMode.MANUAL || ackMode == AckMode.MANUAL_IMMEDIATE;
    }
}