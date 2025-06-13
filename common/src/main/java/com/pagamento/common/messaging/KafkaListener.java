/* ========================================================
# Anotação: KafkaListener
# Módulo: Messaging System - Kafka Message Consumption
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring Kafka 2.7, Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para configurar métodos como listeners de mensagens Kafka.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Mensagens recebidas dos tópicos configurados</li>
 *   <li><b>Processamento:</b> Método anotado é invocado com a mensagem</li>
 *   <li><b>Saída:</b> Confirmação (ACK) ou tratamento de erro</li>
 * </ol>
 * 
 * <p>Componentes envolvidos:</p>
 * <ul>
 *   <li>KafkaListenerContainerFactory - Cria o container do listener</li>
 *   <li>KafkaMessageListenerContainer - Gerencia o ciclo de vida</li>
 *   <li>ConsumerFactory - Configura o consumidor Kafka subjacente</li>
 * </ul>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Schema(description = "Marca um método como listener de mensagens Kafka")
public @interface KafkaListener {

    /**
     * Tópicos Kafka para assinar.
     * 
     * @return Nomes dos tópicos
     * 
     * @Schema(
     *     description = "Tópicos Kafka para assinatura",
     *     example = "{\"pagamentos\", \"notificacoes\"}",
     *     required = true
     * )
     */
    String[] topics() default {};

    /**
     * ID do grupo de consumidores.
     * 
     * @return ID do grupo
     * 
     * @Schema(
     *     description = "ID do grupo de consumidores",
     *     example = "\"pagamento-service-group\"",
     *     defaultValue = "\"${spring.kafka.consumer.group-id}\""
     * )
     */
    String groupId() default "";

    /**
     * ID do container do listener.
     * 
     * @return ID do container
     * 
     * @Schema(
     *     description = "ID único para o container do listener",
     *     example = "\"pagamentoListenerContainer\""
     * )
     */
    String id() default "";

    /**
     * Número de threads concorrentes.
     * 
     * @return Número de listeners paralelos
     * 
     * @Schema(
     *     description = "Nível de concorrência (número de threads listener)",
     *     example = "3",
     *     defaultValue = "1"
     * )
     */
    int concurrency() default 1;

    /**
     * Configuração de auto-startup.
     * 
     * @return Se o listener deve iniciar automaticamente
     * 
     * @Schema(
     *     description = "Se o container deve iniciar automaticamente",
     *     example = "true",
     *     defaultValue = "true"
     * )
     */
    boolean autoStartup() default true;

    /**
     * Pattern para matching de tópicos.
     * 
     * @return Pattern regex para tópicos
     * 
     * @Schema(
     *     description = "Pattern regex para assinatura de tópicos",
     *     example = "\"pagamento.*\""
     * )
     */
    String topicPattern() default "";

    /**
     * Partições específicas para escutar.
     * 
     * @return Array de partições
     * 
     * @Schema(
     *     description = "Partições específicas para assinar",
     *     example = "{0, 1}"
     * )
     */
    int[] partitions() default {};
}