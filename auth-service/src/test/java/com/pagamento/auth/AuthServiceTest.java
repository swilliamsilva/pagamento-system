package com.pagamento.auth;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.pagamento.auth.dto.AuthRequest;
import com.pagamento.auth.dto.AuthResponse;
import com.pagamento.auth.entity.User;
import com.pagamento.auth.exception.InvalidCredentialsException;
import com.pagamento.auth.exception.InvalidTokenException;
import com.pagamento.auth.exception.UserNotFoundException;
import com.pagamento.auth.repository.UserRepository;
import com.pagamento.auth.service.AuthService;
import com.pagamento.auth.service.TokenProvider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_ValidCredentials_ReturnsTokens() {
        // Arrange
        AuthRequest request = new AuthRequest("user", "password");
        User user = new User();
        user.setUsername("user");
        user.setRoles("ROLE_USER");
        
        when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        when(userRepository.findByUsername("user"))
            .thenReturn(Optional.of(user));
        when(tokenProvider.createAccessToken("user")).thenReturn("accessToken");
        when(tokenProvider.createRefreshToken("user")).thenReturn("refreshToken");

        // Act
        AuthResponse response = authService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("accessToken", response.getToken());
        assertEquals("refreshToken", response.getToken());
        assertEquals("ROLE_USER", response.getRoles());
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        // Arrange
        AuthRequest request = new AuthRequest("user", "wrongpassword");
        
        when(authenticationManager.authenticate(any()))
            .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> authService.authenticate(request));
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        // Arrange
        AuthRequest request = new AuthRequest("unknown", "password");
        
        when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        when(userRepository.findByUsername("unknown"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authService.authenticate(request));
    }

    @Test
    void refreshToken_ValidToken_ReturnsNewAccessToken() {
        // Arrange
        String refreshToken = "valid.refresh.token";
        Claims claims = Jwts.claims().setSubject("user");
        User user = new User();
        user.setUsername("user");
        user.setRoles("ROLE_ADMIN");
        
        when(tokenProvider.validateAndExtractClaims(refreshToken)).thenReturn(claims);
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(tokenProvider.createAccessToken("user")).thenReturn("newAccessToken");

        // Act
        AuthResponse response = authService.refreshToken(refreshToken);

        // Assert
        assertNotNull(response);
        assertEquals("newAccessToken", response.getToken());
        assertEquals(refreshToken, response.getToken());
        assertEquals("ROLE_ADMIN", response.getRoles());
    }

    @Test
    void refreshToken_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.token";
        
        when(tokenProvider.validateAndExtractClaims(invalidToken))
            .thenThrow(new InvalidTokenException("Invalid token"));

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> authService.refreshToken(invalidToken));
    }

    @Test
    void refreshToken_UserNotFound_ThrowsException() {
        // Arrange
        String refreshToken = "valid.token";
        Claims claims = Jwts.claims().setSubject("unknown");
        
        when(tokenProvider.validateAndExtractClaims(refreshToken)).thenReturn(claims);
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> authService.refreshToken(refreshToken));
    }
}