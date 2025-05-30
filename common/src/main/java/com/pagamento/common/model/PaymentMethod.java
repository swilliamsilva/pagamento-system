package com.pagamento.common.model;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {
    @Id
    private UUID id;
    private String type; // CREDIT_CARD, DEBIT_CARD, PIX, BOLETO
    private String details; // JSON com dados específicos
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public PaymentMethod() {
        // UUID será gerado pelo provedor de persistência
    }

    public PaymentMethod(String type, String details, User user) {
        this.type = type;
        this.details = details;
        this.user = user;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
