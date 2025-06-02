package com.pagamento.auth;

import com.pagamento.common.exception.AuthenticationException;
import com.pagamento.entity.User;
import com.pagamento.repository.UserRepository;
import com.pagamento.service.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_ValidUser_ReturnsToken() {
        UserRepository user = new UserRepository("user@example.com", "encodedPass", "USER");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("validPass123", "encodedPass")).thenReturn(true);
        when(tokenProvider.createToken(anyString(), anyList())).thenReturn("jwt.token.xyz");
        
        var response = authService.authenticate("user@example.com", "validPass123");
        
        assertNotNull(response);
        assertEquals("jwt.token.xyz", response.getToken());
        assertEquals("SUCCESS", response.getStatus());
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        UserRepository user = new UserRepository("user@example.com", "encodedPass", "USER");
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);
        
        assertThrows(AuthenticationException.class, () -> 
            authService.authenticate("user@example.com", "wrongPass")
        );
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        
        assertThrows(AuthenticationException.class, () -> 
            authService.authenticate("unknown@example.com", "anyPass")
        );
    }

    @Test
    void authenticate_InactiveUser_ThrowsException() {
        UserRepository user = new UserRepository("inactive@example.com", "encodedPass", "USER");
        user.setActive(false);
        
        when(userRepository.findByEmail("inactive@example.com")).thenReturn(Optional.of(user));
        
        assertThrows(AuthenticationException.class, () -> 
            authService.authenticate("inactive@example.com", "validPass")
        );
    }
}
