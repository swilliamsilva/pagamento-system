package com.pagamento.common.response;

import java.util.Date;

public class PaymentResponse {
    private String transactionId;
    private String status;
    private String code;
    private Date timestamp;
    private String message;

    public PaymentResponse() {}

    public PaymentResponse(String transactionId, String status, String code, Date timestamp, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.code = code;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
