package com.pagamento.common.dto;

import java.io.Serializable;
import java.util.Date;

public class LogMessageDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String level;
    private String mensagem;
    private String classe;
    private String metodo;
    private Date timestamp;

    public LogMessageDTO() {
        this.timestamp = new Date(); // define timestamp automaticamente
    }

    public LogMessageDTO(String level, String mensagem, String classe, String metodo) {
        this.level = level;
        this.mensagem = mensagem;
        this.classe = classe;
        this.metodo = metodo;
        this.timestamp = new Date();
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long millis) {
        this.timestamp = new Date(millis);
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
