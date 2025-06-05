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

    private String status = "PENDENTE";

    public Boleto() {}

    public Boleto(String codigoBarras, BigDecimal valor, LocalDate dataVencimento, String beneficiario, String pagador) {
        this.codigoBarras = codigoBarras;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.beneficiario = beneficiario;
        this.pagador = pagador;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getPagador() {
        return pagador;
    }

    public void setPagador(String pagador) {
        this.pagador = pagador;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
