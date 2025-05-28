package com.pagamento.pagbank.service;

import com.pagamento.pagbank.dto.PaymentRequest;
import com.pagamento.pagbank.dto.PaymentResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PagBankServiceTest {

    @Test
    public void testSendPayment() {
        PagBankService service = new PagBankService();
        PaymentRequest request = new PaymentRequest();
        request.setAmount("100.00");
        request.setPayerId("cliente-123");
        request.setDescription("Compra de Teste");

        PaymentResponse response = service.sendPayment(request);

        assertNotNull(response);
        assertTrue(response.getStatus() != null || response.getMessage() != null);
    }

    @Test
    public void testCheckStatus() {
        PagBankService service = new PagBankService();
        PaymentResponse response = service.checkStatus("txn123");

        assertNotNull(response);
        assertTrue(response.getStatus() != null || response.getMessage() != null);
    }

    @Test
    public void testRefundPayment() {
        PagBankService service = new PagBankService();
        PaymentRequest request = new PaymentRequest();
        request.setAmount("100.00");
        request.setPayerId("cliente-123");

        PaymentResponse response = service.refundPayment(request);

        assertNotNull(response);
        assertTrue(response.getStatus() != null || response.getMessage() != null);
    }
}
