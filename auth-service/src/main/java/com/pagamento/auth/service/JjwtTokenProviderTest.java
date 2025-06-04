package com.pagamento.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JjwtTokenProviderTest {

    private JjwtTokenProvider tokenProvider;
    private final String secret = "secretKeyThatIsLongEnoughForHS512Algorithm12345";
    private final long expiration = 86400000; // 1 dia

    @BeforeEach
    void setUp() {
        tokenProvider = new JjwtTokenProvider(secret, expiration, expiration * 30);
    }

    @Test
    void createAccessToken_ValidUsername_ReturnsToken() {
        // Act
        String token = tokenProvider.createAccessToken("testuser");
        
        // Assert
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // Verifica estrutura JWT
    }

    @Test
    void validateAndExtractClaims_ValidToken_ReturnsClaims() {
        // Arrange
        String token = tokenProvider.createAccessToken("testuser");
        
        // Act
        Claims claims = tokenProvider.validateAndExtractClaims(token);
        
        // Assert
        assertEquals("testuser", claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void validateAndExtractClaims_InvalidToken_ThrowsException() {
        // Arrange
        String invalidToken = "invalid.token";
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> tokenProvider.validateAndExtractClaims(invalidToken));
    }

    @Test
    void createRefreshToken_DifferentExpiration_ReturnsToken() {
        // Act
        String accessToken = tokenProvider.createAccessToken("user");
        String refreshToken = tokenProvider.createRefreshToken("user");
        
        // Assert
        assertNotEquals(accessToken, refreshToken);
        
        Claims accessClaims = tokenProvider.validateAndExtractClaims(accessToken);
        Claims refreshClaims = tokenProvider.validateAndExtractClaims(refreshToken);
        
        assertTrue(refreshClaims.getExpiration().after(accessClaims.getExpiration()));
    }
}