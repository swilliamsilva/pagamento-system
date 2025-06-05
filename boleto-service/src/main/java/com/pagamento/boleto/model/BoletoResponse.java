package com.pagamento.boleto.model;

import java.time.LocalDateTime;

public class BoletoResponse {
    private String codigoBarras;
    private String numeroControle;
    private LocalDateTime dataHoraPagamento;
    private String status;
    private String mensagem;

    public BoletoResponse(String codigoBarras, String numeroControle, 
                          LocalDateTime dataHoraPagamento, String status, String mensagem) {
        this.codigoBarras = codigoBarras;
        this.numeroControle = numeroControle;
        this.dataHoraPagamento = dataHoraPagamento;
        this.status = status;
        this.mensagem = mensagem;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public String getNumeroControle() {
        return numeroControle;
    }

    public LocalDateTime getDataHoraPagamento() {
        return dataHoraPagamento;
    }

    public String getStatus() {
        return status;
    }

    public String getMensagem() {
        return mensagem;
    }
}
