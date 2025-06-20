package com.pagamento.common.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class AppUser {

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

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

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

    public List<PaymentMethod> getPaymentMethods() { return paymentMethods; }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods.forEach(pm -> pm.setAppUser(null));
        if (paymentMethods != null) {
            paymentMethods.forEach(pm -> pm.setAppUser(this));
        }
        this.paymentMethods = paymentMethods;
    }

    public void addPaymentMethod(PaymentMethod paymentMethod) {
        if (!this.paymentMethods.contains(paymentMethod)) {
            paymentMethod.setAppUser(this);
            this.paymentMethods.add(paymentMethod);
        }
    }

    public void removePaymentMethod(PaymentMethod paymentMethod) {
        paymentMethod.setAppUser(null);
        this.paymentMethods.remove(paymentMethod);
    }
}
