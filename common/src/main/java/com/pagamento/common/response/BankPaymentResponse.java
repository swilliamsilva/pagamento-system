package com.pagamento.common.response;

import java.util.Date;

public class BankPaymentResponse {
    private String status;
    private String transactionId;
    private String message;
    private Date timestamp;
    private String bankAuthorizationCode;

    public BankPaymentResponse() {}

    public BankPaymentResponse(String status, String transactionId, String message, Date timestamp, String bankAuthorizationCode) {
        this.status = status;
        this.transactionId = transactionId;
        this.message = message;
        this.timestamp = timestamp;
        this.bankAuthorizationCode = bankAuthorizationCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getBankAuthorizationCode() {
        return bankAuthorizationCode;
    }

    public void setBankAuthorizationCode(String bankAuthorizationCode) {
        this.bankAuthorizationCode = bankAuthorizationCode;
    }
}
