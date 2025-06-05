package com.pagamento.payment.contract;

import au.com.dius.pact.consumer.PactProviderRuleMk2;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.consumer.junit.PactRunner;
import au.com.dius.pact.consumer.junit.PactVerificationInvocationContextProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import static org.junit.Assert.*;

@RunWith(PactRunner.class)
@Provider("paymentProvider")
@Consumer("paymentConsumer")
public class PaymentContractTest {

    @Rule
    public PactProviderRuleMk2 mockProvider =
            new PactProviderRuleMk2("paymentProvider", "localhost", 8080, this);

    private final String requestJson = new JSONObject()
            .put("amount", 100.00)
            .put("currency", "BRL")
            .toString();

    @Pact(consumer = "paymentConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
            .given("payment processing service is available")
            .uponReceiving("a request to process payment")
                .path("/api/v1/payments")
                .method("POST")
                .headers("Content-Type", "application/json")
                .body(requestJson)
            .willRespondWith()
                .status(201)
                .headers("Content-Type", "application/json")
                .body(new JSONObject()
                        .put("transactionId", "txn_123")
                        .put("status", "COMPLETED")
                        .toString())
            .toPact();
    }

    @Test
    @PactVerification
    public void testCreatePayment() {
        String url = mockProvider.getUrl() + "/api/v1/payments";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = new RestTemplate().exchange(
            url, HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        JSONObject responseJson = new JSONObject(response.getBody());
        assertEquals("txn_123", responseJson.getString("transactionId"));
        assertEquals("COMPLETED", responseJson.getString("status"));
    }
}
