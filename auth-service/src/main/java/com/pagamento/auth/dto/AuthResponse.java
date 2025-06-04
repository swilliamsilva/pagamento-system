package com.pagamento.auth.dto;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String roles;

    public AuthResponse() {}

    public AuthResponse(String token, String refreshToken, String roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

	public Object getToken() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getRoles() {
		// TODO Auto-generated method stub
		return null;
	}

    // Getters e Setters
}