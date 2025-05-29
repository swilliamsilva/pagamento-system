package com.pagamento.caixa.controller;

import com.pagamento.caixa.dto.PaymentRequest;
import com.pagamento.caixa.dto.PaymentResponse;
import com.pagamento.caixa.service.CaixaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/caixa")
public class CaixaController {

    @Autowired
    private CaixaService caixaService;

    @PostMapping("/pay")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return caixaService.sendPayment(request);
    }

    @GetMapping("/status/{transactionId}")
    public PaymentResponse getStatus(@PathVariable String transactionId) {
        return caixaService.checkStatus(transactionId);
    }

    @PostMapping("/refund")
    public PaymentResponse refund(@RequestBody PaymentRequest request) {
        return caixaService.refundPayment(request);
    }
}
