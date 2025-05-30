package com.pagamento.gateway.service;

import com.pagamento.gateway.config.GatewayConfig;
import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GatewayService {
    private final GatewayFactory gatewayFactory;
    private final GatewayConfig gatewayConfig;

    @Autowired
    public GatewayService(GatewayFactory gatewayFactory, GatewayConfig gatewayConfig) {
        this.gatewayFactory = gatewayFactory;
        this.gatewayConfig = gatewayConfig;
    }

    public GatewayPaymentResponse processPayment(GatewayPaymentRequest request) {
        // Usar provider padrão se não especificado
        String provider = request.getProvider() != null ? 
                          request.getProvider() : 
                          gatewayConfig.getDefaultProvider();
        
        // Obter gateway apropriado
        PaymentGateway gateway = gatewayFactory.getGateway(provider);
        
        // Processar pagamento
        return gateway.processPayment(request);
    }
}
