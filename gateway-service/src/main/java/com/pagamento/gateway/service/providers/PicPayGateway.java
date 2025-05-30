package com.pagamento.gateway.service.providers;

import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;
import com.pagamento.gateway.service.PaymentGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PicPayGateway implements PaymentGateway {
    private static final String PROVIDER_NAME = "PICPAY";
    
    @Override
    public GatewayPaymentResponse processPayment(GatewayPaymentRequest request) {
        GatewayPaymentResponse response = new GatewayPaymentResponse();
        response.setProvider(PROVIDER_NAME);
        response.setTimestamp(LocalDateTime.now());
        
        // Simular processamento
        Random rand = new Random();
        response.setTransactionId("PIC" + System.currentTimeMillis() + rand.nextInt(1000));
        
        // Simular falha (5% chance)
        if (rand.nextDouble() < 0.05) {
            response.setStatus("FAILED");
            response.setMessage("PicPay processing error");
            response.setSuccess(false);
            response.setProviderResponse("ErrorCode: PIC-500");
        } else {
            response.setStatus("COMPLETED");
            response.setMessage("Pagamento processado com sucesso pelo PicPay");
            response.setSuccess(true);
            response.setProviderResponse("AuthorizationCode: PIC" + rand.nextInt(1000000));
        }
        
        return response;
    }

    @Override
    public boolean supports(String provider) {
        return PROVIDER_NAME.equalsIgnoreCase(provider);
    }
}
