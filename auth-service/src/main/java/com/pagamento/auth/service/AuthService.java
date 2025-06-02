package com.pagamento.auth.service;

import com.pagamento.auth.dto.AuthRequest;
import com.pagamento.auth.dto.AuthResponse;
import com.pagamento.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    private final String SECRET = "meuSegredoSuperSecreto";
    private final long EXPIRATION_TIME = 864_000_000; // 10 dias
    private final long REFRESH_EXPIRATION = 2_592_000_000; // 30 dias

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = ((AuthenticationManager) authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        UserRepository user = ((Object) userRepository.findByUsername(request.getUsername()))
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        
        String token = generateToken(((AuthRequest) user).getUsername());
        String refreshToken = generateRefreshToken(user.getUsername());
        
        return new AuthResponse(token, refreshToken, user.getRoles());
    }

    public AuthResponse refreshToken(String refreshToken) {
        // Lógica de validação do refresh token
        String username = ((Object) Jwts.parser())
            .setSigningKey(SECRET)
            .parseClaimsJws(refreshToken)
            .getBody()
            .getSubject();
            
        return new AuthResponse(generateToken(username), refreshToken, "USER");
    }
    
    private String generateToken(String username) {
        return ((Object) Jwts.builder())
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }
    
    private String generateRefreshToken(String username) {
        return ((Object) Jwts.builder())
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();
    }
}
