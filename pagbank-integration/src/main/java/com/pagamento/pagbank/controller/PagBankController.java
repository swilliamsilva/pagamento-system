package com.pagamento.pagbank.controller;

import com.pagamento.pagbank.dto.PaymentRequest;
import com.pagamento.pagbank.dto.PaymentResponse;
import com.pagamento.pagbank.service.PagBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagbank")
public class PagBankController {

    @Autowired
    private PagBankService pagBankService;

    @PostMapping("/pay")
    public PaymentResponse processPayment(@RequestBody PaymentRequest request) {
        return pagBankService.sendPayment(request);
    }

    @GetMapping("/status/{transactionId}")
    public PaymentResponse getStatus(@PathVariable String transactionId) {
        return pagBankService.checkStatus(transactionId);
    }

    @PostMapping("/refund")
    public PaymentResponse refund(@RequestBody PaymentRequest request) {
        return pagBankService.refundPayment(request);
    }
}
