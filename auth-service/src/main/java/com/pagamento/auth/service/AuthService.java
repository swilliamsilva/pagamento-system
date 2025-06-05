package com.pagamento.auth.service;

import com.pagamento.auth.dto.AuthRequest;
import com.pagamento.auth.dto.AuthResponse;
import com.pagamento.auth.entity.User;
import com.pagamento.auth.exception.InvalidCredentialsException;
import com.pagamento.auth.exception.InvalidTokenException;
import com.pagamento.auth.exception.UserNotFoundException;
import com.pagamento.auth.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Autowired
    public AuthService(
        UserRepository userRepository,
        AuthenticationManager authenticationManager,
        TokenProvider tokenProvider
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse authenticate(com.pagamento.auth.dto.AuthRequest authRequest) {
        if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
            throw new InvalidCredentialsException("Username e password são obrigatórios");
        }
        
        try {
    
            
            // Busca o usuário no banco de dados
            User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
            
            // Gera os tokens usando o TokenProvider
            String token = tokenProvider.createAccessToken(user.getUsername());
            String refreshToken = tokenProvider.createRefreshToken(user.getUsername());
            
            return new AuthResponse(token, refreshToken, user.getRoles());
              } catch (BadCredentialsException e) {
                throw new InvalidCredentialsException("Credenciais inválidas");
              } catch (AuthenticationException e) {
                throw new InvalidCredentialsException("Falha na autenticação", e);
        }
    }      
       

    public AuthResponse refreshToken(String refreshToken) {
        try {
            // Valida o refresh token e extrai as claims
            Claims claims = tokenProvider.validateAndExtractClaims(refreshToken);
            String username = claims.getSubject();
            
            // Busca o usuário
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
            
            // Gera novo token de acesso
            String newToken = tokenProvider.createAccessToken(username);
            
            return new AuthResponse(newToken, refreshToken, user.getRoles());
            
        } catch (Exception e) {
            throw new InvalidTokenException("Refresh token inválido ou expirado", e);
        }
    }
}