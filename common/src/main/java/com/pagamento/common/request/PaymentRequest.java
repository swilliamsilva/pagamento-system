package com.pagamento.common.request;

import java.math.BigDecimal;

public class PaymentRequest {
    private BigDecimal amount;
    private String currency;
    private String referenceId;
    private String paymentMethodId;

    public PaymentRequest() {}

    public PaymentRequest(BigDecimal amount, String currency, String referenceId, String paymentMethodId) {
        this.amount = amount;
        this.currency = currency;
        this.referenceId = referenceId;
        this.paymentMethodId = paymentMethodId;
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

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
