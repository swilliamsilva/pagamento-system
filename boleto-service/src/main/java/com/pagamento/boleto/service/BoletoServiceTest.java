package com.pagamento.boleto.service;

import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.dto.BoletoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoletoServiceTest {

    @InjectMocks
    private BoletoService boletoService;

    @Test
    void processarBoleto_ValidBoleto_ReturnsPago() {
        // Arrange
        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.valueOf(100.50),
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );

        // Act
        BoletoResponse response = boletoService.processarBoleto(boleto);

        // Assert
        assertEquals("PAGO", response.getStatus());
        assertTrue(response.getNumeroControle().startsWith("CTRL-"));
        assertEquals("Pagamento realizado com sucesso", response.getMensagem());
    }

    @Test
    void processarBoleto_ExpiredBoleto_ReturnsRejeitado() {
        // Arrange
        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.valueOf(100.50),
            LocalDate.now().minusDays(1), // Vencido
            "Beneficiário",
            "Pagador"
        );

        // Act
        BoletoResponse response = boletoService.processarBoleto(boleto);

        // Assert
        assertEquals("REJEITADO", response.getStatus());
        assertEquals("Boleto inválido ou vencido", response.getMensagem());
    }

    @Test
    void processarBoleto_InvalidBarcode_ReturnsRejeitado() {
        // Arrange
        Boleto boleto = new Boleto(
            "123", // Código inválido
            BigDecimal.valueOf(100.50),
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );

        // Act
        BoletoResponse response = boletoService.processarBoleto(boleto);

        // Assert
        assertEquals("REJEITADO", response.getStatus());
    }

    @Test
    void validarBoleto_ValidInput_ReturnsTrue() {
        // Arrange
        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.valueOf(100.50),
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );

        // Act
        boolean isValid = boletoService.validarBoleto(boleto);

        // Assert
        assertTrue(isValid);
    }
}