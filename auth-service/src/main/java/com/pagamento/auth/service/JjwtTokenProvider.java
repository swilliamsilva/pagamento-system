package com.pagamento.auth.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JjwtTokenProvider implements TokenProvider {

    private final String secret;
    private final long accessExpiration;
    private final long refreshExpiration;

    public JjwtTokenProvider(
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.expiration}") long accessExpiration,
        @Value("${jwt.refresh.expiration}") long refreshExpiration
    ) {
        this.secret = secret;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public String createAccessToken(String username) {
        return buildToken(username, accessExpiration);
    }

    @Override
    public String createRefreshToken(String username) {
        return buildToken(username, refreshExpiration);
    }

    @Override
    public Claims validateAndExtractClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }

    private String buildToken(String username, long expiration) {
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }
}