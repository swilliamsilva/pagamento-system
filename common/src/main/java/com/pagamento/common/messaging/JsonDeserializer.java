/* ========================================================
# Classe: JsonDeserializer
# Módulo: Messaging System - JSON Deserialization
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Tecnologias: Java 8, Spring 2.7, Jackson 2.13 e Maven - Junho de 2025
# ======================================================== */

package com.pagamento.common.messaging;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Configurações para desserialização segura de mensagens JSON.
 * 
 * <p>Fluxo principal:</p>
 * <ol>
 *   <li><b>Entrada:</b> Mensagem JSON serializada</li>
 *   <li><b>Processamento:</b> Conversão para objeto Java usando estas configurações</li>
 *   <li><b>Saída:</b> Objeto desserializado ou exceção em caso de falha</li>
 * </ol>
 * 
 * <p>Classes envolvidas:</p>
 * <ul>
 *   <li>KafkaJsonDeserializer - Implementação concreta do desserializador</li>
 *   <li>ObjectMapper - Componente Jackson para manipulação JSON</li>
 *   <li>ConsumerFactory - Configura os desserializadores para os consumidores</li>
 * </ul>
 */
@Schema(description = "Configurações para desserialização segura de mensagens JSON")
public class JsonDeserializer {

    /**
     * Pacotes permitidos para desserialização (prevenção RCE).
     * 
     * @Schema(description = "Lista de pacotes confiáveis para desserialização",
     *         example = "\"com.pagamento.dto,com.shared.models\"",
     *         defaultValue = "\"com.pagamento\"")
     */
    public static final String TRUSTED_PACKAGES = "spring.json.trusted.packages";

    /**
     * Habilita uso de headers para informação de tipo.
     * 
     * @Schema(description = "Se deve usar headers __TypeId__ para inferência de tipo",
     *         example = "true",
     *         defaultValue = "true")
     */
    public static final String USE_TYPE_INFO_HEADERS = "spring.json.use.type.headers";

    /**
     * Tipo padrão quando não há informação de tipo.
     * 
     * @Schema(description = "Tipo padrão para desserialização quando não especificado",
     *         example = "\"com.pagamento.dto.PagamentoDTO\"",
     *         defaultValue = "\"java.util.Map\"")
     */
    public static final String VALUE_DEFAULT_TYPE = "spring.json.value.default.type";

    /**
     * Configuração para tratamento de propriedades desconhecidas.
     * 
     * @Schema(description = "Se deve falhar em propriedades JSON não mapeadas",
     *         example = "false",
     *         defaultValue = "true")
     */
    public static final String FAIL_ON_UNKNOWN_PROPERTIES = "spring.json.fail.on.unknown.properties";

    /**
     * Cria configuração padrão segura para desserialização.
     * 
     * @return Configuração padrão como Properties
     * 
     * @Schema(description = "Cria configuração padrão segura para JSON",
     *         example = "{\"spring.json.trusted.packages\":\"com.pagamento\"}")
     */
    public static java.util.Properties createDefaultConfig() {
        java.util.Properties props = new java.util.Properties();
        props.put(TRUSTED_PACKAGES, "com.pagamento");
        props.put(USE_TYPE_INFO_HEADERS, "true");
        props.put(VALUE_DEFAULT_TYPE, "java.util.Map");
        props.put(FAIL_ON_UNKNOWN_PROPERTIES, "false");
        return props;
    }
}