/* ========================================================
# Classe de Teste: AdminClientTest
# Módulo: pagamento-common-health
# Autor: William Silva
# Tecnologias: Java 8, Spring Boot 2.7, JUnit 4, Mockito
# Descrição: Testes dos endpoints de health check
# ======================================================== */

package com.pagamento.common.health;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class AdminClientTest {

    private MockMvc mockMvc;

    @Before
    public void setup() {
        AdminClient adminClient = new AdminClient();
        this.mockMvc = MockMvcBuilders.standaloneSetup(adminClient).build();
    }

    @Test
    public void testBasicHealthCheck() throws Exception {
        mockMvc.perform(get("/admin/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application is UP"));
    }

    @Test
    public void testDetailedHealthCheck() throws Exception {
        mockMvc.perform(get("/admin/health/detailed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.db.status").value("UP"))
                .andExpect(jsonPath("$.components.externalService.status").value("UP"));
    }
}
