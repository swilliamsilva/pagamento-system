package boletoTest;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.model.BoletoResponse;
import com.pagamento.boleto.service.BoletoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BoletoServiceTestFull {

    private BoletoService boletoService;

    @BeforeEach
    void setup() {
        boletoService = new BoletoService();
    }

    @Test
    void validBoletoShouldReturnPago() {
        Boleto boleto = new Boleto("12345678901234567890123456789012345678901234", BigDecimal.valueOf(10), LocalDate.now().plusDays(1), "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertNotNull(response);
        assertNotEquals("REJEITADO", response.getStatus());
    }

    @Test
    void boletoWithNullBarcodeShouldBeRejected() {
        Boleto boleto = new Boleto(null, BigDecimal.TEN, LocalDate.now().plusDays(1), "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void boletoWithShortBarcodeShouldBeRejected() {
        Boleto boleto = new Boleto("1234", BigDecimal.TEN, LocalDate.now().plusDays(1), "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void boletoWithNullAmountShouldBeRejected() {
        Boleto boleto = new Boleto("12345678901234567890123456789012345678901234", null, LocalDate.now().plusDays(1), "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void boletoWithZeroAmountShouldBeRejected() {
        Boleto boleto = new Boleto("12345678901234567890123456789012345678901234", BigDecimal.ZERO, LocalDate.now().plusDays(1), "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void boletoWithPastDueDateShouldBeRejected() {
        Boleto boleto = new Boleto("12345678901234567890123456789012345678901234", BigDecimal.TEN, LocalDate.now().minusDays(1), "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void boletoWithNullDueDateShouldBeRejected() {
        Boleto boleto = new Boleto("12345678901234567890123456789012345678901234", BigDecimal.TEN, null, "Beneficiario", "Pagador");
        BoletoResponse response = boletoService.processarBoleto(boleto);
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void gerarNumeroControleFormatIsCorrect() {
        String ctrl = boletoService.processarBoleto(new Boleto("12345678901234567890123456789012345678901234", BigDecimal.TEN, LocalDate.now().plusDays(1), "B", "P")).getNumeroControle();
        assertTrue(ctrl.startsWith("CTRL-"));
        assertEquals(13, ctrl.length());
    }

    @Test
    void multipleValidBoletosShouldProcessSuccessfully() {
        for (int i = 0; i < 10; i++) {
            Boleto boleto = new Boleto("12345678901234567890123456789012345678901234", BigDecimal.valueOf(i + 1), LocalDate.now().plusDays(1), "B", "P");
            BoletoResponse response = boletoService.processarBoleto(boleto);
            assertNotEquals("REJEITADO", response.getStatus());
        }
    }

    @Test
    void multipleInvalidBoletosShouldBeRejected() {
        for (int i = 0; i < 10; i++) {
            Boleto boleto = new Boleto("123", BigDecimal.ZERO, LocalDate.now().minusDays(1), "", "");
            BoletoResponse response = boletoService.processarBoleto(boleto);
            assertEquals("REJEITADO", response.getStatus());
        }
    }
} 
