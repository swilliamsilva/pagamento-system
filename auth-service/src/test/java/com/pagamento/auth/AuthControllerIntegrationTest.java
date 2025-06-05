package com.pagamento.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.pagamento.auth.controller.AuthController;
import com.pagamento.auth.dto.AuthRequest;
import com.pagamento.auth.dto.AuthResponse;
import com.pagamento.auth.exception.InvalidCredentialsException;
import com.pagamento.auth.service.AuthService;


@WebMvcTest(AuthController.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    void login_ValidCredentials_ReturnsTokens() throws Exception {
        // Arrange
        AuthResponse response = new AuthResponse("token", "refreshToken", "ROLE_USER");
        when(authService.authenticate(any(AuthRequest.class))).thenReturn(response);

        String requestBody = "{\"username\":\"user\",\"password\":\"pass\"}";

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"))
                .andExpect(jsonPath("$.roles").value("ROLE_USER"));
    }

    @Test
    void login_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        // Arrange
        when(authService.authenticate(any(AuthRequest.class)))
            .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        String requestBody = "{\"username\":\"user\",\"password\":\"wrongpass\"}";

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("Credenciais inválidas"));
    }
}