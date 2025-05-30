package com.pagamento.auth;


import com.pagamento.common.request.AuthRequest;
import com.pagamento.common.response.AuthResponse;
import com.pagamento.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void authenticate_ValidCredentials_ReturnsToken() throws Exception {
        AuthRequest request = new AuthRequest("user@example.com", "validPass123");
        AuthResponse response = new AuthResponse("jwt.token.xyz", "SUCCESS", "Authenticated");
        
        Mockito.when(authService.authenticate(request)).thenReturn(response);
        
        mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.xyz"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void authenticate_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        AuthRequest request = new AuthRequest("user@example.com", "wrongPass");
        
        Mockito.when(authService.authenticate(request))
            .thenThrow(new SecurityException("Invalid credentials"));
        
        mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void authenticate_BruteForce_BlocksAfter5Attempts() throws Exception {
        AuthRequest request = new AuthRequest("attacker@example.com", "wrongPass");
        
        // Primeiras 5 tentativas
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/v1/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
        
        // 6ª tentativa deve ser bloqueada
        mockMvc.perform(post("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isTooManyRequests());
    }
}
