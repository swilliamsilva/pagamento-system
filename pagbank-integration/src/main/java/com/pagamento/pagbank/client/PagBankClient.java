package com.pagamento.pagbank.client;

import com.pagamento.pagbank.dto.PaymentRequest;
import com.pagamento.pagbank.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pagbankClient", url = "https://api.pagbank.uol.com", fallback = PagBankFallback.class)
public interface PagBankClient {

    @PostMapping("/v1/payments")
    PaymentResponse sendPayment(@RequestBody PaymentRequest request);

    @GetMapping("/v1/status/{transactionId}")
    PaymentResponse checkStatus(@PathVariable("transactionId") String transactionId);

    @PostMapping("/v1/refund")
    PaymentResponse refundPayment(@RequestBody PaymentRequest request);
}
