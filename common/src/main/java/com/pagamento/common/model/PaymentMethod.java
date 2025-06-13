package com.pagamento.common.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    public enum Type {
        CREDIT_CARD, DEBIT_CARD, PIX, BOLETO, PLATFORM
    }

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Type type;

    @Column(nullable = false, length = 255)
    private String details;

    @Column(nullable = false, length = 100)
    private String bank;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    // --- Constructors ---
    public PaymentMethod() {}

    public PaymentMethod(Type type, String details, String bank, AppUser appUser) {
        this.type = type;
        this.details = details;
        this.bank = bank;
        this.appUser = appUser;
    }

    // --- Getters and Setters ---
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getBank() { return bank; }
    public void setBank(String bank) { this.bank = bank; }

    public AppUser getAppUser() { return appUser; }
    public void setAppUser(AppUser appUser) { this.appUser = appUser; }

    // --- equals & hashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaymentMethod)) return false;
        PaymentMethod that = (PaymentMethod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", type=" + type +
                ", bank='" + bank + '\'' +
                '}';
    }
}
