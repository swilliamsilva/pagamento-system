package com.pagamento.common.dto;

import java.util.List;

public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String role;
    private List<String> paymentMethods;

    // Construtores
    public UserDTO() {}
    
    public UserDTO(String id, String username, String email, String role, List<String> paymentMethods) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.paymentMethods = paymentMethods;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<String> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(List<String> paymentMethods) { this.paymentMethods = paymentMethods; }
}
