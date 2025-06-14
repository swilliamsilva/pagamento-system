package com.pagamento.common.messaging;

import org.junit.Before;
import org.junit.Test;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class KafkaProducerConfigTest {

    private KafkaProducerConfig config;

    @Before
    public void setUp() throws Exception {
        config = new KafkaProducerConfig();
        // Injetar bootstrapServers via reflection
        Field field = KafkaProducerConfig.class.getDeclaredField("bootstrapServers");
        field.setAccessible(true);
        field.set(config, "localhost:9092");
    }

    @Test
    public void testProducerFactoryConfiguration() {
        ProducerFactory<String, Object> pf = config.producerFactory();
        assertTrue(pf instanceof DefaultKafkaProducerFactory);

        DefaultKafkaProducerFactory<String, Object> factory = (DefaultKafkaProducerFactory<String, Object>) pf;
        Map<String, Object> props = factory.getConfigurationProperties();

        // Verifica configurações principais
        assertEquals("localhost:9092", props.get("bootstrap.servers"));
        assertEquals("all", props.get("acks"));
        assertEquals(3, props.get("retries"));
        assertEquals(true, props.get("enable.idempotence"));

        // Verifica JSON trusted packages configurado corretamente
        assertEquals("com.pagamento.dto,com.pagamento.common.dto", props.get("spring.json.trusted.packages"));

        // Verifica timeouts
        assertEquals(30000, props.get("delivery.timeout.ms"));
        assertEquals(15000, props.get("request.timeout.ms"));
    }
}
