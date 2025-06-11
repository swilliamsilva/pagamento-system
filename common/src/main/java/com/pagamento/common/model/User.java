package com.pagamento.common.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String passwordHash;
    
    @Column(nullable = false)
    private String role;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentMethod> paymentMethods;

    // Getters e Setters (padrão)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // Métodos para gerenciar paymentMethods
    public List<PaymentMethod> getPaymentMethods() { 
        return paymentMethods; 
    }
    
    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        if (this.paymentMethods != null) {
            this.paymentMethods.forEach(pm -> pm.setUser(null));
        }
        if (paymentMethods != null) {
            paymentMethods.forEach(pm -> pm.setUser(this));
        }
        this.paymentMethods = paymentMethods;
    }
    
    public void addPaymentMethod(PaymentMethod paymentMethod) {
        paymentMethod.setUser(this);
        this.paymentMethods.add(paymentMethod);
    }
    
    public void removePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethod.setUser(null);
        this.paymentMethods.remove(paymentMethod);
    }
}