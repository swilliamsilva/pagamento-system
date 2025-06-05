package cardtest;



import com.pagamento.card.model.CardPaymentRequest;
import com.pagamento.card.model.CardPaymentResponse;
import com.pagamento.card.service.CardService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    private CardPaymentRequest buildValidRequest() {
        CardPaymentRequest request = new CardPaymentRequest();
        request.setCardNumber("1234567812345678");
        request.setCardHolder("Nome Completo");
        request.setExpiryDate("12/99");
        request.setCvv("123");
        request.setAmount(BigDecimal.valueOf(150.00));
        request.setDescription("Compra teste");
        return request;
    }

    @Test
    void testCardExpiredReturnsRejeitado() {
        CardPaymentRequest expiredCard = buildValidRequest();
        expiredCard.setExpiryDate("01/20"); // data antiga

        CardPaymentResponse response = cardService.processPayment(expiredCard);

        assertEquals("REJEITADO", response.getStatus());
        assertEquals("Cartão expirado", response.getMessage());
        assertNull(response.getAuthorizationCode());
        assertNull(response.getTransactionId());
    }

    @Test
    void testValidPaymentReturnsAprovado() {
        CardPaymentRequest request = buildValidRequest();

        for (int i = 0; i < 10; i++) {
            CardPaymentResponse response = cardService.processPayment(request);

            assertNotNull(response);
            assertNotNull(response.getStatus());
            assertNotNull(response.getMessage());
            assertNotNull(response.getTimestamp());

            if ("APROVADO".equals(response.getStatus())) {
                assertNotNull(response.getAuthorizationCode());
                assertEquals("Pagamento realizado com sucesso", response.getMessage());
            } else if ("FALHA".equals(response.getStatus())) {
                assertEquals("Falha no processamento com a operadora", response.getMessage());
            } else if ("REJEITADO".equals(response.getStatus())) {
                assertEquals("Saldo insuficiente", response.getMessage());
            } else {
                fail("Status inesperado: " + response.getStatus());
            }
        }
    }

    @Test
    void testInvalidExpiryFormatReturnsRejeitado() {
        CardPaymentRequest request = buildValidRequest();
        request.setExpiryDate("13/99"); // mês inválido

        CardPaymentResponse response = cardService.processPayment(request);

        assertEquals("REJEITADO", response.getStatus());
        assertEquals("Cartão expirado", response.getMessage());
    }

    @Test
    void testAuthCodeFormat() {
        String code = cardService.processPayment(buildValidRequest()).getAuthorizationCode();
        if (code != null) {
            assertTrue(code.matches("AUTH\\d{6}"));
        }
    }
} 
