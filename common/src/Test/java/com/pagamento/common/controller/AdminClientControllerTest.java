package com.pagamento.common.controller;

/* ========================================================
# Classe de Teste: AdminClientControllerTest
# Módulo: pagamento-common-health
# Tecnologias: Java 8, Spring Boot 2.7, JUnit 4, Mockito
# ======================================================== */



import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AdminClientControllerTest {

    private HealthEndpoint healthEndpoint;
    private AdminClientController controller;

    @Before
    public void setUp() {
        healthEndpoint = mock(HealthEndpoint.class);
        controller = new AdminClientController(healthEndpoint);
    }

    @Test
    public void testBasicHealthCheckReturnsUpMessage() {
        String result = controller.basicHealthCheck();
        assertEquals("Application is UP", result);
    }

    @Test
    public void testDetailedHealthCheckReturnsHealthObject() {
        Health mockHealth = Health.up().withDetail("db", "UP").build();
        when(healthEndpoint.health()).thenReturn(mockHealth);

        Health result = (Health) controller.detailedHealthCheck();
        assertNotNull(result);
        assertEquals("UP", result.getStatus().getCode());
        assertEquals("UP", result.getDetails().get("db"));
    }
}
