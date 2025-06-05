package boletoTest;



import com.pagamento.boleto.controller.BoletoController;
import com.pagamento.boleto.model.BoletoResponse;
import com.pagamento.boleto.service.BoletoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BoletoController.class)
class BoletoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoletoService boletoService;

    @Test
    void pagarBoleto_ValidRequest_ReturnsOk() throws Exception {
        // Arrange
        BoletoResponse response = new BoletoResponse(
            "12345678901234567890123456789012345678901234",
            "CTRL-12345",
            LocalDateTime.now(),
            "PAGO",
            "Sucesso"
        );
        
        when(boletoService.processarBoleto(any())).thenReturn(response);

        String requestBody = "{\"codigoBarras\":\"12345678901234567890123456789012345678901234\"," +
            "\"valor\":100.50," +
            "\"dataVencimento\":\"2023-12-31\"," +
            "\"beneficiario\":\"Beneficiário\"," +
            "\"pagador\":\"Pagador\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/boletos/pagar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAGO"))
                .andExpect(jsonPath("$.numeroControle").value("CTRL-12345"));
    }

    @Test
    void pagarBoleto_InvalidRequest_ReturnsBadRequest() throws Exception {
        // Arrange - Request sem código de barras
        String requestBody = "{\"valor\":100.50," +
            "\"dataVencimento\":\"2023-12-31\"," +
            "\"beneficiario\":\"Beneficiário\"," +
            "\"pagador\":\"Pagador\"}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/boletos/pagar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros[0]").value("Código de barras é obrigatório"));
    }
}