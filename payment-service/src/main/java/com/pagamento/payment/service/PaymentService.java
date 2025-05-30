package com.pagamento.payment.service;

import com.pagamento.payment.dto.PaymentRequestDTO;
import com.pagamento.payment.dto.PaymentResponseDTO;
import com.pagamento.payment.mapper.PaymentMapper;
import com.pagamento.payment.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentMapper paymentMapper;

    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        // Converter DTO para entidade
        Payment payment = paymentMapper.toEntity(dto);
        
        // Processar pagamento
        payment.setTransactionId(generateTransactionId());
        
        if (simulateGatewayIntegration()) {
            payment.setStatus("COMPLETED");
        } else {
            payment.setStatus("FAILED");
            throw new RuntimeException("Payment gateway failure");
        }
        
        // Converter entidade para DTO de resposta
        return paymentMapper.toDTO(payment);
    }

    private String generateTransactionId() {
        return "TX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean simulateGatewayIntegration() {
        return Math.random() > 0.1;
    }
}
