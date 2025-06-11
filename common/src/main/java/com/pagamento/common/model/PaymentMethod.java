package com.pagamento.common.model;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class PaymentMethod {
    public enum Type { CREDIT_CARD, DEBIT_CARD, PIX, BOLETO, PLATFORM }
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    
    @Column(nullable = false)
    private String details;
    
    @Column(nullable = false)
    private String bank;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getBank() { return bank; }
    public void setBank(String bank) { this.bank = bank; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}