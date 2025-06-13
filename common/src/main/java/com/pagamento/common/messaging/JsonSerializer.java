/* ========================================================
# Classe: JsonSerializer
# Módulo: Messaging System - JSON Serialization
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Jackson 2.13 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Configurações para serialização de objetos Java em mensagens JSON.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Objeto Java a ser serializado</li>
 *   <li><b>Processamento:</b> Conversão para JSON usando estas configurações</li>
 *   <li><b>Saída:</b> Mensagem JSON serializada</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaJsonSerializer - Implementação concreta do serializador</li>
 *   <li>ObjectMapper - Componente Jackson para manipulação JSON</li>
 *   <li>ProducerFactory - Configura os serializadores para os produtores</li>
 * </ul>
 */
@Schema(description = "Configurações para serialização de objetos em JSON")
public class JsonSerializer {

    /**
     * Configuração para adição de headers de tipo (__TypeId__).
     * 
     * @Schema(description = "Se deve adicionar headers com informação de tipo",
     *         example = "true",
     *         defaultValue = "true",
     *         requiredMode = Schema.RequiredMode.REQUIRED)
     */
    public static final String ADD_TYPE_INFO_HEADERS = "spring.json.add.type.headers";

    /**
     * Configuração para serialização de valores nulos.
     * 
     * @Schema(description = "Se deve incluir campos com valores nulos",
     *         example = "false",
     *         defaultValue = "false")
     */
    public static final String INCLUDE_NULL_VALUES = "spring.json.include.null.values";

    /**
     * Configuração para formato de data.
     * 
     * @Schema(description = "Formato de serialização para datas",
     *         example = "\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\"",
     *         defaultValue = "\"yyyy-MM-dd'T'HH:mm:ss.SSSZ\"")
     */
    public static final String DATE_FORMAT = "spring.json.date.format";

    /**
     * Cria configuração padrão para serialização JSON.
     * 
     * @return Configuração padrão como Properties
     * 
     * @Schema(description = "Cria configuração padrão para serialização JSON",
     *         example = "{\"spring.json.add.type.headers\":true}")
     */
    public static java.util.Properties createDefaultConfig() {
        java.util.Properties props = new java.util.Properties();
        props.put(ADD_TYPE_INFO_HEADERS, "true");
        props.put(INCLUDE_NULL_VALUES, "false");
        props.put(DATE_FORMAT, "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return props;
    }

    /**
     * Configuração para compactação do JSON.
     * 
     * @Schema(description = "Se deve compactar o JSON resultante",
     *         example = "true",
     *         defaultValue = "false")
     */
    public static final String ENABLE_COMPRESSION = "spring.json.enable.compression";
}