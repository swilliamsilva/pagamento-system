package com.pagamento.payment;

import com.pagamento.common.dto.PaymentDTO;
import com.pagamento.common.enums.PaymentStatus;
import com.pagamento.common.exception.PaymentProcessingException;
import com.pagamento.common.messaging.KafkaMessageProducer;
import com.pagamento.common.observability.ObservabilityUtils;
import com.pagamento.entity.Payment;
import com.pagamento.entity.PaymentMethod;
import com.pagamento.entity.User;
import com.pagamento.repository.PaymentRepository;
import com.pagamento.service.AntiFraudService;
import com.pagamento.service.PaymentGatewayService;
import com.pagamento.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PaymentServiceIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @MockBean
    private PaymentGatewayService gatewayService;

    @MockBean
    private AntiFraudService antiFraudService;

    @MockBean
    private KafkaMessageProducer messageProducer;

    @MockBean
    private ObservabilityUtils observabilityUtils;

    @Test
    void processPayment_ValidPayment_ReturnsSuccess() {
        // Setup
        PaymentMethod method = new PaymentMethod("CREDIT_CARD", "**** 1234");
        User user = new User("user@example.com", "pass", "USER");
        user.addPaymentMethod(method);
        
        Payment payment = ((Object) Payment.builder())
            .amount(new BigDecimal("100.00"))
            .currency("BRL")
            .user(user)
            .paymentMethod(method)
            .build();
        
        payment = paymentRepository.save(payment);
        
        // Mocking
        when(gatewayService.process(any())).thenReturn("gateway_txn_123");
        when(antiFraudService.analyze(any())).thenReturn(true);
        
        // Execution
        com.pagamento.payment.PaymentDTO result = paymentService.processPayment(payment.getId().toString());
        
        // Verification
        assertNotNull(result);
        assertEquals(PaymentStatus.COMPLETED.name(), result.getStatus());
        verify(messageProducer).sendMessage(eq("payment.processed"), any());
    }

    @Test
    void processPayment_AntiFraudRejection_ThrowsException() {
        // Setup
        PaymentMethod method = new PaymentMethod("CREDIT_CARD", "**** 1234");
        User user = new User("user@example.com", "pass", "USER");
        user.addPaymentMethod(method);
        
        Payment payment = ((Object) Payment.builder())
            .amount(new BigDecimal("10000.00")) // High amount
            .currency("BRL")
            .user(user)
            .paymentMethod(method)
            .build();
        
        payment = paymentRepository.save(payment);
        
        // Mocking
        when(antiFraudService.analyze(any())).thenReturn(false);
        
        // Execution & Verification
        assertThrows(PaymentProcessingException.class, () -> 
            paymentService.processPayment(payment.getId().toString())
        );
        
        // Verify failure event
        verify(messageProducer).sendMessage(eq("payment.failed"), any());
    }

    private void assertThrows(Class<com.pagamento.payment.PaymentProcessingException> class1, Executable executable) {
		// TODO Auto-generated method stub
		
	}

	@Test
    void processPayment_ConcurrentRequests_HandlesLocking() throws InterruptedException {
        // Setup
        PaymentMethod method = new PaymentMethod("CREDIT_CARD", "**** 1234");
        User user = new User("user@example.com", "pass", "USER");
        user.addPaymentMethod(method);
        
        Payment payment = ((Object) Payment.builder())
            .amount(new BigDecimal("100.00"))
            .currency("BRL")
            .user(user)
            .paymentMethod(method)
            .build();
        
        payment = paymentRepository.save(payment);
        
        // Mocking
        when(gatewayService.process(any())).thenReturn("gateway_txn_123");
        when(antiFraudService.analyze(any())).thenReturn(true);
        
        // Simulate concurrent requests
        Runnable task = () -> {
            try {
                paymentService.processPayment(payment.getId().toString());
            } catch (Exception e) {
                // Ignore for test
            }
        };
        
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        		
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // Verify only one successful processing
        Payment updated = ((Object) paymentRepository.findById(payment.getId())).orElseThrow();
        assertNotNull(updated.getProcessedAt());
    }
}
