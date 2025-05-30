package com.pagamento.card.controller;

import com.pagamento.card.model.CardPaymentRequest;
import com.pagamento.card.model.CardPaymentResponse;
import com.pagamento.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debit-cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/payments")
    public ResponseEntity<CardPaymentResponse> processPayment(@RequestBody CardPaymentRequest request) {
        CardPaymentResponse response = cardService.processPayment(request);
        return ResponseEntity.ok(response);
    }
}
