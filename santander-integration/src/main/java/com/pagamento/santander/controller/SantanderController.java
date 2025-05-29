package com.pagamento.santander.controller;

import com.pagamento.santander.dto.PaymentRequest;
import com.pagamento.santander.dto.PaymentResponse;
import com.pagamento.santander.service.SantanderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/santander")
public class SantanderController {

    @Autowired
    private SantanderService santanderService;

    @PostMapping("/pay")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return santanderService.sendPayment(request);
    }

    @GetMapping("/status/{transactionId}")
    public PaymentResponse getStatus(@PathVariable String transactionId) {
        return santanderService.checkStatus(transactionId);
    }

    @PostMapping("/refund")
    public PaymentResponse refund(@RequestBody PaymentRequest request) {
        return santanderService.refundPayment(request);
    }
}
