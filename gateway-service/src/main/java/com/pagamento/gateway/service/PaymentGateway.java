package com.pagamento.gateway.service;

import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;

public interface PaymentGateway {
    GatewayPaymentResponse processPayment(GatewayPaymentRequest request);
    boolean supports(String provider);
}
