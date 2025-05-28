package com.pagamento.pagbank.service;

import com.pagamento.pagbank.dto.PaymentRequest;
import com.pagamento.pagbank.dto.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PagBankService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.pagbank.uol.com")
            .build();

    public PaymentResponse sendPayment(PaymentRequest req) {
        return webClient.post()
                .uri("/v1/payments")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    public PaymentResponse checkStatus(String transactionId) {
        return webClient.get()
                .uri("/v1/status/" + transactionId)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    public PaymentResponse refundPayment(PaymentRequest req) {
        return webClient.post()
                .uri("/v1/refund")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }
}
