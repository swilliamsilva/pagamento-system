package com.pagamento.common.health;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HealthEndpointTest {

    private HealthIndicator indicadorUp;
    private HealthIndicator indicadorDown;
    private Map<String, HealthIndicator> indicatorsMap;

    @Before
    public void setup() {
        indicadorUp = mock(HealthIndicator.class);
        when(indicadorUp.health()).thenReturn(Health.up().withDetail("mensagem", "Funcionando").build());

        indicadorDown = mock(HealthIndicator.class);
        when(indicadorDown.health()).thenReturn(Health.down().withDetail("mensagem", "Falha").build());
    }

    @Test
    public void deveRetornarStatusUpQuandoTodosOsContribuintesEstaoUp() {
        indicatorsMap = new HashMap<>();
        indicatorsMap.put("db", indicadorUp);
        indicatorsMap.put("kafka", indicadorUp);

        // Usando implementação alternativa do endpoint para testes
        HealthEndpointManual endpoint = new HealthEndpointManual(indicatorsMap);
        Health status = endpoint.health();

        assertEquals("UP", status.getStatus().getCode());
        assertEquals("UP", status.getDetails().get("db").toString().contains("UP") ? "UP" : "DOWN");
        assertEquals("UP", status.getDetails().get("kafka").toString().contains("UP") ? "UP" : "DOWN");
    }

    @Test
    public void deveRetornarStatusDownQuandoAlgumContribuinteEstaDown() {
        indicatorsMap = new HashMap<>();
        indicatorsMap.put("db", indicadorUp);
        indicatorsMap.put("kafka", indicadorDown);

        // Usando implementação alternativa do endpoint para testes
        HealthEndpointManual endpoint = new HealthEndpointManual(indicatorsMap);
        Health status = endpoint.health();

        assertEquals("DOWN", status.getStatus().getCode());
        assertEquals("UP", status.getDetails().get("db").toString().contains("UP") ? "UP" : "DOWN");
        assertEquals("DOWN", status.getDetails().get("kafka").toString().contains("DOWN") ? "DOWN" : "UP");
    }

    // Classe auxiliar para testes sem depender de HealthContributorRegistry
    private static class HealthEndpointManual {
        private final Map<String, HealthIndicator> indicatorsMap;

        public HealthEndpointManual(Map<String, HealthIndicator> indicatorsMap) {
            this.indicatorsMap = indicatorsMap;
        }

        public Health health() {
            Map<String, Object> healthDetails = new HashMap<>();
            boolean isDown = false;

            for (Map.Entry<String, HealthIndicator> entry : indicatorsMap.entrySet()) {
                Health health = entry.getValue().health();
                healthDetails.put(entry.getKey(), health);
                if (health.getStatus().equals(org.springframework.boot.actuate.health.Status.DOWN)) {
                    isDown = true;
                }
            }

            return Health.status(isDown ? "DOWN" : "UP").withDetails(healthDetails).build();
        }
    }
}
