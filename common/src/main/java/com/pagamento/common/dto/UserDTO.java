package com.pagamento.common.dto;

import java.util.List;

public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String role;
    private List<PaymentMethodSummaryDTO> paymentMethods;

    // Construtor padrão
    public UserDTO() {}

    // Construtor completo
    public UserDTO(String id, String username, String email, String role, 
                 List<PaymentMethodSummaryDTO> paymentMethods) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.paymentMethods = paymentMethods;
    }

    // Builder
    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private String id;
        private String username;
        private String email;
        private String role;
        private List<PaymentMethodSummaryDTO> paymentMethods;

        public UserDTOBuilder id(String id) {
            this.id = id;
            return this;
        }
        public UserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }
        public UserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }
        public UserDTOBuilder role(String role) {
            this.role = role;
            return this;
        }
        public UserDTOBuilder paymentMethods(List<PaymentMethodSummaryDTO> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }
        public UserDTO build() {
            return new UserDTO(id, username, email, role, paymentMethods);
        }
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
    public List<PaymentMethodSummaryDTO> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(List<PaymentMethodSummaryDTO> paymentMethods) { 
        this.paymentMethods = paymentMethods; 
    }
}