package com.pagamento.pix.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class PixResponse {
    private UUID idTransacao;
    private String chave;
    private double valor;
    private LocalDateTime dataHora;
    private String status;
    private String qrCode; // Representação simplificada
    private String mensagem;

    public PixResponse() {}

    public PixResponse(UUID idTransacao, String chave, double valor, 
                       LocalDateTime dataHora, String status, 
                       String qrCode, String mensagem) {
        this.idTransacao = idTransacao;
        this.chave = chave;
        this.valor = valor;
        this.dataHora = dataHora;
        this.status = status;
        this.qrCode = qrCode;
        this.mensagem = mensagem;
    }

    // Getters e Setters
    public UUID getIdTransacao() { return idTransacao; }
    public void setIdTransacao(UUID idTransacao) { this.idTransacao = idTransacao; }
    public String getChave() { return chave; }
    public void setChave(String chave) { this.chave = chave; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
