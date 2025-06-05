package com.pagamento.payment.service;

import com.pagamento.payment.dto.PaymentRequestDTO;
import com.pagamento.payment.dto.PaymentResponseDTO;
import com.pagamento.payment.model.Payment;
import com.pagamento.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void processPayment_ValidRequest_ReturnsCompletedResponse() {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setAmount(new BigDecimal("150.00"));
        request.setCurrency("BRL");

        Payment mockPayment = new Payment(UUID.randomUUID().toString(), request.getAmount(), request.getCurrency());
        mockPayment.setStatus("COMPLETED");

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        PaymentResponseDTO response = paymentService.processPayment(request);

        assertNotNull(response);
        assertEquals("COMPLETED", response.getStatus());
        assertEquals("BRL", response.getCurrency());
        assertEquals(new BigDecimal("150.00"), response.getAmount());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void processPayment_NullAmount_ThrowsException() {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setCurrency("BRL");

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
    }

    @Test
    void processPayment_NullCurrency_ThrowsException() {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setAmount(new BigDecimal("50.00"));

        assertThrows(IllegalArgumentException.class, () -> paymentService.processPayment(request));
    }
}
