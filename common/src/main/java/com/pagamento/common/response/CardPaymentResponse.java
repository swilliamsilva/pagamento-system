package com.pagamento.common.response;

import java.util.Date;

public class CardPaymentResponse {
    private String status;
    private String transactionId;
    private String authorizationCode;
    private String message;
    private Date timestamp;
    private String cardLastFour;
    private String cardBrand;

    public CardPaymentResponse() {}

    public CardPaymentResponse(String status, String transactionId, String authorizationCode, 
                              String message, Date timestamp, String cardLastFour, String cardBrand) {
        this.status = status;
        this.transactionId = transactionId;
        this.authorizationCode = authorizationCode;
        this.message = message;
        this.timestamp = timestamp;
        this.cardLastFour = cardLastFour;
        this.cardBrand = cardBrand;
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

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
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

    public String getCardLastFour() {
        return cardLastFour;
    }

    public void setCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }
}
