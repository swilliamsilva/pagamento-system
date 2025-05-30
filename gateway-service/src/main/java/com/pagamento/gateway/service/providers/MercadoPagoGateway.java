package com.pagamento.gateway.service.providers;

import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;
import com.pagamento.gateway.service.PaymentGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MercadoPagoGateway implements PaymentGateway {
    private static final String PROVIDER_NAME = "MERCADOPAGO";
    
    @Override
    public GatewayPaymentResponse processPayment(GatewayPaymentRequest request) {
        GatewayPaymentResponse response = new GatewayPaymentResponse();
        response.setProvider(PROVIDER_NAME);
        response.setTimestamp(LocalDateTime.now());
        
        // Simular processamento
        Random rand = new Random();
        response.setTransactionId("MP" + System.currentTimeMillis() + rand.nextInt(1000));
        
        // Simular falha (4% chance)
        if (rand.nextDouble() < 0.04) {
            response.setStatus("FAILED");
            response.setMessage("MercadoPago processing error");
            response.setSuccess(false);
            response.setProviderResponse("ErrorCode: MP-500");
        } else {
            response.setStatus("COMPLETED");
            response.setMessage("Pagamento processado com sucesso pelo MercadoPago");
            response.setSuccess(true);
            response.setProviderResponse("AuthorizationCode: MP" + rand.nextInt(1000000));
        }
        
        return response;
    }

    @Override
    public boolean supports(String provider) {
        return PROVIDER_NAME.equalsIgnoreCase(provider);
    }
}
