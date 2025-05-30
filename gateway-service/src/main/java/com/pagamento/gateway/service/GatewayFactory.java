package com.pagamento.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GatewayFactory {
    private final List<PaymentGateway> gateways;

    @Autowired
    public GatewayFactory(List<PaymentGateway> gateways) {
        this.gateways = gateways;
    }

    public PaymentGateway getGateway(String provider) {
        return gateways.stream()
                .filter(gateway -> gateway.supports(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Provedor de pagamento não suportado: " + provider));
    }
}
