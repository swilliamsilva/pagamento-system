package com.pagamento.gateway.service.providers;

import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;
import com.pagamento.gateway.service.PaymentGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PagSeguroGateway implements PaymentGateway {
    private static final String PROVIDER_NAME = "PAGSEGURO";
    
    @Override
    public GatewayPaymentResponse processPayment(GatewayPaymentRequest request) {
        GatewayPaymentResponse response = new GatewayPaymentResponse();
        response.setProvider(PROVIDER_NAME);
        response.setTimestamp(LocalDateTime.now());
        
        // Simular processamento
        Random rand = new Random();
        response.setTransactionId("PAG" + System.currentTimeMillis() + rand.nextInt(1000));
        
        // Simular falha (3% chance)
        if (rand.nextDouble() < 0.03) {
            response.setStatus("FAILED");
            response.setMessage("PagSeguro processing error");
            response.setSuccess(false);
            response.setProviderResponse("ErrorCode: PAG-500");
        } else {
            response.setStatus("COMPLETED");
            response.setMessage("Pagamento processado com sucesso pelo PagSeguro");
            response.setSuccess(true);
            response.setProviderResponse("AuthorizationCode: PAG" + rand.nextInt(1000000));
        }
        
        return response;
    }

    @Override
    public boolean supports(String provider) {
        return PROVIDER_NAME.equalsIgnoreCase(provider);
    }
}
