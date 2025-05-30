package com.pagamento.common.dto;

import java.math.BigDecimal;

public class CardDTO {
    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;
    private BigDecimal amount;
    private String description;

    // Construtores
    public CardDTO() {}
    
    public CardDTO(String cardNumber, String cardHolder, String expiryDate, 
                  String cvv, BigDecimal amount, String description) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
    }

    // Getters e Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
