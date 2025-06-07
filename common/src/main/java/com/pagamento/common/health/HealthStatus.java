package com.pagamento.common.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * ========================================================
 * UTILITÁRIO DE STATUS DE SAÚDE
 *
 * @tag Health Utilities
 * @summary Helper para construção de respostas de health check
 */
public class HealthStatus {

    /**
     * @operation up
     * @summary Cria status UP
     */
    public static Builder up() {
        return Health.up();
    }

    /**
     * @operation down
     * @summary Cria status DOWN
     */
    public static Builder down() {
        return Health.down();
    }

    /**
     * @operation customStatus
     * @param status Nome do status customizado (ex: "WARNING", "TIMEOUT")
     * @summary Cria status personalizado via String
     */
    public static Builder status(String status) {
        return Health.status(status);
    }
}

/**
 * ========================================================
 * FLUXO DE OPERAÇÃO - HEALTH STATUS HELPER
 * ========================================================
 * 
 * 1. ENTRADA:
 *    - Chamada a um dos métodos estáticos (up(), down() ou status())
 *    - Parâmetros opcionais para detalhes adicionais
 * 
 * 2. PROCESSAMENTO:
 *    - Cria um objeto Builder do Spring Boot Actuator Health
 *    - Permite adição de detalhes através do padrão builder:
 *      .withDetail("chave", "valor")
 * 
 * 3. SAÍDA:
 *    - Objeto Health pronto para uso com:
 *      * Status (UP, DOWN ou customizado)
 *      * Detalhes opcionais (Map<String, Object>)
 * 
 * ========================================================
 * RELACIONAMENTOS COM OUTRAS CLASSES
 * ========================================================
 * 
 * +------------------------+-------------------+-----------------------------------+
 * | Classe                 | Tipo de Relação   | Descrição                         |
 * +------------------------+-------------------+-----------------------------------+
 * | Health (Spring)        | Retorno           | Classe do Spring Boot Actuator    |
 * | Health.Builder         | Utilização        | Padrão builder para construção    |
 * | ExternalServiceChecker | Utilização        | Classe principal de health check  |
 * | HealthIndicator        | Contexto          | Interface implementada pelo caller|
 * +------------------------+-------------------+-----------------------------------+
 * 
 * ========================================================
 * EXEMPLOS DE USO
 * ========================================================
 * 
 * 1. Status simples UP:
 *    HealthStatus.up().build();
 * 
 * 2. Status DOWN com detalhes:
 *    HealthStatus.down()
 *       .withDetail("error", "Connection timeout")
 *       .withDetail("service", "PaymentGateway")
 *       .build();
 * 
 * 3. Status customizado:
 *    HealthStatus.status("WARNING")
 *       .withDetail("message", "High latency")
 *       .withDetail("value", "450ms")
 *       .build();
 */