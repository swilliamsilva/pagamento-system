package com.pagamento.common.response;

public class AuthResponse {
    private String token;
    private String status;
    private String message;
    private String userId;

    public AuthResponse() {}

    public AuthResponse(String token, String status, String message, String userId) {
        this.token = token;
        this.status = status;
        this.message = message;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
