package com.pagamento.boleto.service;

import io.jsonwebtoken.Claims;

public interface TokenProvider {
    String createAccessToken(String username);
    String createRefreshToken(String username);
    Claims validateAndExtractClaims(String token);
}