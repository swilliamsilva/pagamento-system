package com.pagamento.common.observality.health;



import org.junit.Test;
import org.springframework.boot.actuate.health.Health;

import com.pagamento.common.observability.health.KafkaHealthIndicator;

import static org.junit.Assert.*;

public class KafkaHealthIndicatorTest {

    @Test
    public void testKafkaHealthShouldBeUp() {
        KafkaHealthIndicator indicator = new KafkaHealthIndicator();
        Health health = indicator.health();
        assertNotNull(health);
        assertEquals("UP", health.getStatus().getCode());
    }
}
