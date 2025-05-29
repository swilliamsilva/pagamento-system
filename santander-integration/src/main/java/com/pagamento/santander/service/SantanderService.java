package com.pagamento.santander.service;

import com.pagamento.santander.dto.PaymentRequest;
import com.pagamento.santander.dto.PaymentResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SantanderService {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.santander.com.br")
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
                .uri("/v1/payments/" + transactionId)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }

    public PaymentResponse refundPayment(PaymentRequest req) {
        return webClient.post()
                .uri("/v1/payments/refunds")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PaymentResponse.class)
                .block();
    }
}
