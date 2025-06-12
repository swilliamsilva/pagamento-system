package com.pagamento.common.dto;

import com.pagamento.common.enums.PaymentType;

public class PaymentMethodDTO {
    private String id;
    private PaymentType type;
    private String lastFourDigits;
    private String pixKey;
    private String bankName;

    public PaymentMethodDTO() {}

    public PaymentMethodDTO(String id, PaymentType type, String lastFourDigits, String pixKey, String bankName) {
        this.id = id;
        this.type = type;
        this.lastFourDigits = lastFourDigits;
        this.pixKey = pixKey;
        this.bankName = bankName;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public PaymentType getType() { return type; }
    public void setType(PaymentType type) { this.type = type; }

    public String getLastFourDigits() { return lastFourDigits; }
    public void setLastFourDigits(String lastFourDigits) { this.lastFourDigits = lastFourDigits; }

    public String getPixKey() { return pixKey; }
    public void setPixKey(String pixKey) { this.pixKey = pixKey; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
}