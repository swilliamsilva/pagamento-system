package com.pagamento.common.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentDTO {
    private String id;
    private BigDecimal amount;
    private String currency;
    private String status;
    private Date createdAt;
    private Date updatedAt;
    private String userId;
    private String paymentMethodId;
    private String paymentType;

    // Construtores
    public PaymentDTO() {}
    
    public PaymentDTO(String id, BigDecimal amount, String currency, String status, 
                     Date createdAt, Date updatedAt, String userId, String paymentMethodId, 
                     String paymentType) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.paymentMethodId = paymentMethodId;
        this.paymentType = paymentType;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }
}
