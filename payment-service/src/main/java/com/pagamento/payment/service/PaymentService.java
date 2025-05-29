package com.pagamento.payment.service;

import com.pagamento.payment.model.Payment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final Map<String, Payment> payments = new HashMap<>();

    public Payment processPayment(Payment paymentRequest) {
        paymentRequest.setTransactionId(generateTransactionId());
        paymentRequest.setStatus("COMPLETED");
        
        boolean gatewaySuccess = simulateGatewayIntegration();
        
        if(gatewaySuccess) {
            payments.put(paymentRequest.getTransactionId(), paymentRequest);
            return paymentRequest;
        } else {
            throw new RuntimeException("Payment gateway failure");
        }
    }

    private String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean simulateGatewayIntegration() {
        return Math.random() > 0.1;
    }
}
