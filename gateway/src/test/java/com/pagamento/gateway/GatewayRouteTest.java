package com.pagamento.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "gateway.routes.auth-service.url=http://localhost:${wiremock.server.port}/auth-service",
        "gateway.routes.payment-service.url=http://localhost:${wiremock.server.port}/payment-service"
    }
)
@AutoConfigureWireMock(port = 0)
class GatewayRouteTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void authRoute_ValidRequest_ProxiesCorrectly() {
        // Setup WireMock stub
        stubFor(post(urlEqualTo("/auth-service/api/v1/auth"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{"token":"test.token","status":"SUCCESS"}")
                .withStatus(200)));

        // Test request
        webClient.post().uri("/auth/api/v1/auth")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue("{"username":"test","password":"pass"}")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.token").isEqualTo("test.token")
            .jsonPath("$.status").isEqualTo("SUCCESS");
    }

    @Test
    void paymentRoute_ValidRequest_ProxiesCorrectly() {
        // Setup WireMock stub
        stubFor(post(urlEqualTo("/payment-service/api/v1/payments"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{"transactionId":"txn_123","status":"COMPLETED"}")
                .withStatus(201)));

        // Test request
        webClient.post().uri("/payment/api/v1/payments")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue("{"amount":100,"currency":"BRL"}")
            .exchange()
            .expectStatus().isCreated()
            .expectBody()
            .jsonPath("$.transactionId").isEqualTo("txn_123")
            .jsonPath("$.status").isEqualTo("COMPLETED");
    }

    @Test
    void invalidRoute_ReturnsNotFound() {
        webClient.get().uri("/invalid-route")
            .exchange()
            .expectStatus().isNotFound();
    }

    @Test
    void rateLimiting_ExceedsLimit_Returns429() {
        for (int i = 0; i < 100; i++) {
            webClient.get().uri("/auth/api/v1/status")
                .exchange();
        }
        
        webClient.get().uri("/auth/api/v1/status")
            .exchange()
            .expectStatus().isEqualTo(429);
    }
}
