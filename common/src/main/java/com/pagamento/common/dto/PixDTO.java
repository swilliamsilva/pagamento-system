package com.pagamento.common.dto;

import java.math.BigDecimal;

public class PixDTO {
    private String key;
    private BigDecimal amount;
    private String description;
    private String beneficiary;

    public PixDTO() {}
    
    public PixDTO(String key, BigDecimal amount, String description, String beneficiary) {
        this.key = key;
        this.amount = amount;
        this.description = description;
        this.beneficiary = beneficiary;
    }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getBeneficiary() { return beneficiary; }
    public void setBeneficiary(String beneficiary) { this.beneficiary = beneficiary; }
}
