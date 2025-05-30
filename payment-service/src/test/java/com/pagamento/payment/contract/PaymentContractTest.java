package com.pagamento.payment.contract;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.pagamento.common.dto.PaymentDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "paymentProvider", port = "8080")
class PaymentContractTest {

    @Pact(provider = "paymentProvider", consumer = "paymentConsumer")
    public RequestResponsePact createPaymentContract(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        
        return builder
            .given("payment processing service is available")
            .uponReceiving("a request to process payment")
                .path("/api/v1/payments")
                .method("POST")
                .body("{"amount":100.00,"currency":"BRL"}")
            .willRespondWith()
                .status(201)
                .headers(headers)
                .body("{"transactionId":"txn_123","status":"COMPLETED"}")
            .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createPaymentContract")
    void testCreatePayment(MockServer mockServer) {
        // Setup
        String url = mockServer.getUrl() + "/api/v1/payments";
        PaymentDTO request = new PaymentDTO();
        request.setAmount(new BigDecimal("100.00"));
        request.setCurrency("BRL");
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<PaymentDTO> entity = new HttpEntity<>(request, headers);
        
        // Execute
        ResponseEntity<PaymentDTO> response = new RestTemplate().exchange(
            url, HttpMethod.POST, entity, PaymentDTO.class);
        
        // Verify
        assertEquals(201, response.getStatusCodeValue());
        assertEquals("txn_123", response.getBody().getTransactionId());
        assertEquals("COMPLETED", response.getBody().getStatus());
    }
}
