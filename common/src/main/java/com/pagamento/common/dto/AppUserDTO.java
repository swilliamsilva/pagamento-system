package com.pagamento.common.dto;

import java.util.List;



public class AppUserDTO {
    private String id;
    private String username;
    private String email;
    private String role;
    private List<PaymentMethodDTO> paymentMethods;

    public AppUserDTO() {}

    public AppUserDTO(String id, String username, String email, String role,
                      List<PaymentMethodDTO> paymentMethods) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.paymentMethods = paymentMethods;
    }

    public static AppUserDTOBuilder builder() {
        return new AppUserDTOBuilder();
    }

    public static class AppUserDTOBuilder {
        private String id;
        private String username;
        private String email;
        private String role;
        private List<PaymentMethodDTO> paymentMethods;

        public AppUserDTOBuilder id(String id) {
            this.id = id;
            return this;
        }

        public AppUserDTOBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AppUserDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AppUserDTOBuilder role(String role) {
            this.role = role;
            return this;
        }

        public AppUserDTOBuilder paymentMethods(List<PaymentMethodDTO> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public AppUserDTO build() {
            return new AppUserDTO(id, username, email, role, paymentMethods);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<PaymentMethodDTO> getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(List<PaymentMethodDTO> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
