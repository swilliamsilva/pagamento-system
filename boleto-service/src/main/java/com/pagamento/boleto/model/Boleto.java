package com.pagamento.boleto.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Boleto {
    private String codigoBarras;
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private String beneficiario;
    private String pagador;
    private String status; // PENDENTE, PAGO, VENCIDO

    public Boleto() {}

    public Boleto(String codigoBarras, BigDecimal valor, LocalDate dataVencimento, 
                  String beneficiario, String pagador) {
        this.codigoBarras = codigoBarras;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.beneficiario = beneficiario;
        this.pagador = pagador;
        this.status = "PENDENTE";
    }

    // Getters e Setters
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public String getBeneficiario() { return beneficiario; }
    public void setBeneficiario(String beneficiario) { this.beneficiario = beneficiario; }
    public String getPagador() { return pagador; }
    public void setPagador(String pagador) { this.pagador = pagador; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
