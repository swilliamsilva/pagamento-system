package com.pagamento.gateway.controller;

import com.pagamento.gateway.model.GatewayPaymentRequest;
import com.pagamento.gateway.model.GatewayPaymentResponse;
import com.pagamento.gateway.service.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    @Autowired
    private GatewayService gatewayService;

    @PostMapping("/payments")
    public ResponseEntity<GatewayPaymentResponse> processPayment(@RequestBody GatewayPaymentRequest request) {
        GatewayPaymentResponse response = gatewayService.processPayment(request);
        return ResponseEntity.ok(response);
    }
}
