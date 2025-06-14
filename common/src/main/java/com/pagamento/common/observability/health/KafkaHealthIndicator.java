package com.pagamento.common.observability.health;


import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class KafkaHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Simulação: pode haver lógica real de verificação de conexão Kafka
        boolean kafkaAvailable = checkKafka();
        return kafkaAvailable ? Health.up().build() : Health.down().withDetail("Kafka", "Unavailable").build();
    }

    private boolean checkKafka() {
        // Em produção: lógica real
        return true;
    }
}
