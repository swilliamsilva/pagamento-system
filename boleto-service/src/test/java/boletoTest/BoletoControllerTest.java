package boletoTest;




import com.pagamento.boleto.controller.BoletoController;
import com.pagamento.boleto.model.Boleto;
import com.pagamento.boleto.model.BoletoResponse;
import com.pagamento.boleto.service.BoletoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoletoControllerTest {

    @Mock
    private BoletoService boletoService;

    @InjectMocks
    private BoletoController boletoController;

    @Test
    void pagarBoleto_ValidBoleto_ReturnsOk() {
        // Arrange
        Boleto boleto = new Boleto(
            "12345678901234567890123456789012345678901234",
            BigDecimal.valueOf(100.50),
            LocalDate.now().plusDays(1),
            "Beneficiário",
            "Pagador"
        );
        
        BoletoResponse response = new BoletoResponse(
            boleto.getCodigoBarras(),
            "CTRL-12345",
            null,
            "PAGO",
            "Sucesso"
        );
        
        when(boletoService.processarBoleto(any(Boleto.class))).thenReturn(response);

        // Act
        ResponseEntity<BoletoResponse> result = boletoController.pagarBoleto(boleto);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("PAGO", result.getBody().getStatus());
    }

    @Test
    void pagarBoleto_InvalidBoleto_ReturnsBadRequest() {
        // Arrange
        Boleto boleto = new Boleto(); // Inválido
        
        // O Spring Validation retornará 400 automaticamente
        // Não precisamos mockar o serviço pois a requisição nem chegará nele

        // Act
        ResponseEntity<BoletoResponse> result = boletoController.pagarBoleto(boleto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}