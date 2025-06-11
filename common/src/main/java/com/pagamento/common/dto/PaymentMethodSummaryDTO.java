package com.pagamento.common.dto;

import com.pagamento.common.model.PaymentMethod.Type;

public class PaymentMethodSummaryDTO {
    private String id;
    private Type type;
    private String lastFourDigits;
    private String pixKey;
    private String bankName;

    // Construtor padrão
    public PaymentMethodSummaryDTO() {}

    // Construtor completo
    public PaymentMethodSummaryDTO(String id, Type type, String lastFourDigits, String pixKey, String bankName) {
        this.id = id;
        this.type = type;
        this.lastFourDigits = lastFourDigits;
        this.pixKey = pixKey;
        this.bankName = bankName;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }
    public String getLastFourDigits() { return lastFourDigits; }
    public void setLastFourDigits(String lastFourDigits) { this.lastFourDigits = lastFourDigits; }
    public String getPixKey() { return pixKey; }
    public void setPixKey(String pixKey) { this.pixKey = pixKey; }
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
}