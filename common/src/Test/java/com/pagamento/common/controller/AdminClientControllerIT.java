/* ========================================================
# Classe de Teste: AdminClientControllerIT
# Tipo: Integração (SpringBootTest)
# Finalidade: Validar endpoints /admin/health e /admin/health/detailed
# ======================================================== */

package com.pagamento.common.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminClientControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testBasicHealthCheck() {
        ResponseEntity<String> response = restTemplate.getForEntity("/admin/health", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Application is UP"));
    }

    @Test
    public void testDetailedHealthCheck() {
        ResponseEntity<Health> response = restTemplate.getForEntity("/admin/health/detailed", Health.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().getStatus().getCode());
    }
}
