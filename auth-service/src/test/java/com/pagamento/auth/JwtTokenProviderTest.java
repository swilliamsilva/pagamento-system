package com.pagamento.auth;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private final String secret = "secret-key-1234567890-secret-key-1234567890";
    private final long validity = 3600000; // 1 hora

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(secret, validity);
    }

    @Test
    void createToken_ValidAuthentication_ReturnsToken() {
        Authentication authentication = new TestAuthentication("user@example.com", 
            Collections.singletonList(new SimpleGrantedAuthority("USER")));
            
        String token = tokenProvider.createToken(authentication);
        
        assertNotNull(token);
        assertTrue(token.length() > 50);
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        String token = createTestToken();
        assertTrue(tokenProvider.validateToken(token));
    }

    @Test
    void validateToken_ExpiredToken_ReturnsFalse() {
        // Token expirado (criado com validade de 1ms)
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider(secret, 1);
        Authentication auth = new TestAuthentication("user@example.com", 
            Collections.singletonList(new SimpleGrantedAuthority("USER")));
        
        String token = expiredTokenProvider.createToken(auth);
        
        // Esperar 10ms para garantir expiração
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        
        assertFalse(tokenProvider.validateToken(token));
    }

    @Test
    void getUsername_ValidToken_ReturnsUsername() {
        String token = createTestToken();
        assertEquals("user@example.com", tokenProvider.getUsername(token));
    }

    private String createTestToken() {
        Authentication auth = new TestAuthentication("user@example.com", 
            Collections.singletonList(new SimpleGrantedAuthority("USER")));
        return tokenProvider.createToken(auth);
    }

    static class TestAuthentication implements Authentication {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String name;
        private final java.util.Collection<SimpleGrantedAuthority> authorities;

        TestAuthentication(String name, java.util.Collection<SimpleGrantedAuthority> authorities) {
            this.name = name;
            this.authorities = authorities;
        }

        @Override public String getName() { return name; }
        @Override public java.util.Collection<SimpleGrantedAuthority> getAuthorities() { return authorities; }
        @Override public Object getCredentials() { return null; }
        @Override public Object getDetails() { return null; }
        @Override public Object getPrincipal() { return name; }
        @Override public boolean isAuthenticated() { return true; }
        @Override public void setAuthenticated(boolean isAuthenticated) { }
    }
}
