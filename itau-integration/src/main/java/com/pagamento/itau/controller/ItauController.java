package com.pagamento.itau.controller;

import com.pagamento.itau.dto.PaymentRequest;
import com.pagamento.itau.dto.PaymentResponse;
import com.pagamento.itau.service.ItauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/itau")
public class ItauController {

    @Autowired
    private ItauService itauService;

    @PostMapping("/pay")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return itauService.sendPayment(request);
    }

    @GetMapping("/status/{transactionId}")
    public PaymentResponse getStatus(@PathVariable String transactionId) {
        return itauService.checkStatus(transactionId);
    }

    @PostMapping("/refund")
    public PaymentResponse refund(@RequestBody PaymentRequest request) {
        return itauService.refundPayment(request);
    }
}
