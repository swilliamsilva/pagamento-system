package com.pagamento.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void actuatorEndpoints_Unauthenticated_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
            
        mockMvc.perform(get("/actuator/env"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void sqlInjectionAttempt_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/users?name=' OR 1=1--"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void xssAttempt_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/search?query=<script>alert('xss')</script>"))
            .andExpect(status().isBadRequest());
    }
}
