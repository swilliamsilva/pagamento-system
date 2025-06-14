/* ========================================================
# Classe: KafkaLoggerConfig
# Módulo: pagamento-common-messaging
# Autor: William Silva
# Contato: williamsilva.codigo@gmail.com
# Website: simuleagora.com
# ======================================================== */

package com.pagamento.common.messaging;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaLoggerConfig {

    @Bean
    public KafkaLogger kafkaLogger() {
        // Define um logger genérico para logs globais
        return new KafkaLogger(LoggerFactory.getLogger("KafkaLogger"));
    }
}
