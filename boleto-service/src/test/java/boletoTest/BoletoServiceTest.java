package boletoTest;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.model.BoletoResponse;
import com.pagamento.boleto.service.BoletoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoletoServiceTest {

    private BoletoService boletoService;

    @Test
    void processarBoleto_FalhaProcessamento_ReturnsFalha() {
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextDouble()).thenReturn(0.01); // < 0.05 = falha
        boletoService = new BoletoService(mockRandom);

        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.valueOf(100.50),
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );

        BoletoResponse response = boletoService.processarBoleto(boleto);

        assertEquals("FALHA", response.getStatus());
        assertEquals("Falha no processamento do banco", response.getMensagem());
    }

    @Test
    void processarBoleto_ExpiredBoleto_ReturnsRejeitado() {
        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.valueOf(100.50),
            LocalDate.now().minusDays(1),
            "Beneficiário",
            "Pagador"
        );

        BoletoResponse response = boletoService.processarBoleto(boleto);

        assertEquals("REJEITADO", response.getStatus());
        assertEquals("Boleto inválido ou vencido", response.getMensagem());
    }

    @Test
    void processarBoleto_InvalidBarcode_ReturnsRejeitado() {
        Boleto boleto = new Boleto(
            "123",
            BigDecimal.valueOf(100.50),
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );

        BoletoResponse response = boletoService.processarBoleto(boleto);

        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void processarBoleto_ZeroValue_ReturnsRejeitado() {
        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.ZERO,
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );

        BoletoResponse response = boletoService.processarBoleto(boleto);

        assertEquals("REJEITADO", response.getStatus());
    }
}
