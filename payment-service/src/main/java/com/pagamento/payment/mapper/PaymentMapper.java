package com.pagamento.payment.mapper;

import com.pagamento.payment.dto.PaymentRequestDTO;
import com.pagamento.payment.dto.PaymentResponseDTO;
import com.pagamento.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setCurrency(dto.getCurrency());
        return payment;
    }

    public PaymentResponseDTO toDTO(Payment payment) {
        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setTransactionId(payment.getTransactionId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus());
        dto.setMessage("Payment processed successfully");
        return dto;
    }
}
