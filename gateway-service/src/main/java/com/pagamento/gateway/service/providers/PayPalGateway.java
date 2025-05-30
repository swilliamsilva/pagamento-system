package com.pagamento.gateway.service.providers;

import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;
import com.pagamento.gateway.service.PaymentGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PayPalGateway implements PaymentGateway {
    private static final String PROVIDER_NAME = "PAYPAL";
    
    @Override
    public GatewayPaymentResponse processPayment(GatewayPaymentRequest request) {
        GatewayPaymentResponse response = new GatewayPaymentResponse();
        response.setProvider(PROVIDER_NAME);
        response.setTimestamp(LocalDateTime.now());
        
        // Simular processamento
        Random rand = new Random();
        response.setTransactionId("PP" + System.currentTimeMillis() + rand.nextInt(1000));
        
        // Simular falha (6% chance)
        if (rand.nextDouble() < 0.06) {
            response.setStatus("FAILED");
            response.setMessage("PayPal processing error");
            response.setSuccess(false);
            response.setProviderResponse("ErrorCode: PP-500");
        } else {
            response.setStatus("COMPLETED");
            response.setMessage("Pagamento processado com sucesso pelo PayPal");
            response.setSuccess(true);
            response.setProviderResponse("AuthorizationCode: PP" + rand.nextInt(1000000));
        }
        
        return response;
    }

    @Override
    public boolean supports(String provider) {
        return PROVIDER_NAME.equalsIgnoreCase(provider);
    }
}
