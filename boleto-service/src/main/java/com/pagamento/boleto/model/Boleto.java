package com.pagamento.boleto.model;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Boleto {
    
    @NotBlank(message = "Código de barras é obrigatório")
    @Size(min = 44, max = 44, message = "Código de barras deve ter 44 caracteres")
    private String codigoBarras;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    
    @NotNull(message = "Data de vencimento é obrigatória")
    @FutureOrPresent(message = "Data de vencimento deve ser presente ou futura")
    private LocalDate dataVencimento;
    
    @NotBlank(message = "Beneficiário é obrigatório")
    private String beneficiario;
    
    @NotBlank(message = "Pagador é obrigatório")
    private String pagador;
    
    private String status = "PENDENTE"; // PENDENTE, PAGO, VENCIDO

    public Boleto() {}

	public String getCodigoBarras() {
		// TODO Auto-generated method stub
		return null;
	}

	public LocalDate getDataVencimento() {
		// TODO Auto-generated method stub
		return null;
	}

    // Getters e Setters (mantidos)
}