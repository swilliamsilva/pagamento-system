package com.pagamento.common.request;

import java.math.BigDecimal;

public class BankPaymentRequest {
    private String payerId;
    private String bankCode;
    private BigDecimal amount;
    private String description;
    private String cpf;

    public BankPaymentRequest() {}

    public BankPaymentRequest(String payerId, String bankCode, BigDecimal amount, String description, String cpf) {
        this.payerId = payerId;
        this.bankCode = bankCode;
        this.amount = amount;
        this.description = description;
        this.cpf = cpf;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
