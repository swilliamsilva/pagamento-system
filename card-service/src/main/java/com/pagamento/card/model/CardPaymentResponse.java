package com.pagamento.card.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class CardPaymentResponse {
    private UUID transactionId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private String authorizationCode;

    public CardPaymentResponse() {}

    public CardPaymentResponse(UUID transactionId, String status, 
                               String message, LocalDateTime timestamp, 
                               String authorizationCode) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.authorizationCode = authorizationCode;
    }

    // Getters
    public UUID getTransactionId() { return transactionId; }
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getAuthorizationCode() { return authorizationCode; }
}
