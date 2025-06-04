package com.pagamento.auth;

import com.pagamento.auth.dto.AuthRequest;
import com.pagamento.auth.dto.AuthResponse;
import com.pagamento.auth.entity.User;
import com.pagamento.auth.repository.UserRepository;
import com.pagamento.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_WhenValidCredentials_ShouldReturnTokens() {
        // Arrange
        AuthRequest request = new AuthRequest("validUser", "validPass");
        User user = new User();
        user.setUsername("validUser");
        user.setRoles("ROLE_USER");
        
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);
        when(userRepository.findByUsername("validUser"))
            .thenReturn(Optional.of(user));
        
        // Act
        AuthResponse response = authService.authenticate(request);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getToken());
        assertEquals("ROLE_USER", response.getRoles());
        
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByUsername("validUser");
    }

    @Test
    void authenticate_WhenInvalidPassword_ShouldThrowException() {
        // Arrange
        AuthRequest request = new AuthRequest("validUser", "wrongPass");
        
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid credentials"));
        
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> authService.authenticate(request)
        );
        
        assertEquals("Credenciais inválidas", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, never()).findByUsername(anyString());
    }

    @Test
    void authenticate_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        AuthRequest request = new AuthRequest("unknownUser", "anyPass");
        
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);
        when(userRepository.findByUsername("unknownUser"))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> authService.authenticate(request)
        );
        
        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("unknownUser");
    }

    @Test
    void refreshToken_WhenValidRefreshToken_ShouldReturnNewAccessToken() {
        // Arrange
        String refreshToken = "valid.refresh.token";
        String username = "validUser";
        
        User user = new User();
        user.setUsername(username);
        user.setRoles("ROLE_ADMIN");
        
        when(userRepository.findByUsername(username))
            .thenReturn(Optional.of(user));
        
        // Act
        AuthResponse response = authService.refreshToken(refreshToken);
        
        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals(refreshToken, response.getToken());
        assertEquals("ROLE_ADMIN", response.getRoles());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void refreshToken_WhenInvalidRefreshToken_ShouldThrowSecurityException() {
        // Arrange
        String invalidToken = "invalid.token";
        
        // Simular erro de parsing do token
        when(userRepository.findByUsername(anyString())).thenThrow(
            new SecurityException("Refresh token inválido ou expirado")
        );
        
        // Act & Assert
        SecurityException exception = assertThrows(
            SecurityException.class,
            () -> authService.refreshToken(invalidToken)
        );
        
        assertEquals("Refresh token inválido ou expirado", exception.getMessage());
    }
}