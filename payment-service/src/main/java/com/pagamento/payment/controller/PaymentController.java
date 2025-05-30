package com.pagamento.payment.controller;

import com.pagamento.payment.dto.PaymentRequestDTO;
import com.pagamento.payment.dto.PaymentResponseDTO;
import com.pagamento.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(@RequestBody PaymentRequestDTO dto) {
        PaymentResponseDTO response = paymentService.processPayment(dto);
        return ResponseEntity.ok(response);
    }
}
