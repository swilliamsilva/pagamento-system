package com.pagamento.boleto.model;

import java.time.LocalDateTime;

public class BoletoResponse {
    private String codigoBarras;
    private String numeroControle;
    private LocalDateTime dataPagamento;
    private String status;
    private String mensagem;

    public BoletoResponse(String codigoBarras, String numeroControle, 
                          LocalDateTime dataPagamento, String status, String mensagem) {
        this.codigoBarras = codigoBarras;
        this.numeroControle = numeroControle;
        this.dataPagamento = dataPagamento;
        this.status = status;
        this.mensagem = mensagem;
    }

    // Getters
    public String getCodigoBarras() { return codigoBarras; }
    public String getNumeroControle() { return numeroControle; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public String getStatus() { return status; }
    public String getMensagem() { return mensagem; }
}
