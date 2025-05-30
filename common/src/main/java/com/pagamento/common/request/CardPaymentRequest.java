package com.pagamento.common.request;

import java.math.BigDecimal;

public class CardPaymentRequest {
    private String cardNumber;
    private String cardHolder;
    private String expiration;
    private String cvv;
    private BigDecimal amount;
    private String currency;
    private String referenceId;

    public CardPaymentRequest() {}

    public CardPaymentRequest(String cardNumber, String cardHolder, String expiration, 
                             String cvv, BigDecimal amount, String currency, String referenceId) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiration = expiration;
        this.cvv = cvv;
        this.amount = amount;
        this.currency = currency;
        this.referenceId = referenceId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
}
