package com.pagamento.pix.model;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PixRequest {
    @NotBlank(message = "Chave PIX é obrigatória")
    @Size(min = 11, max = 77, message = "Chave PIX deve ter entre 11 e 77 caracteres")
    private String chave; // CPF, email, telefone ou chave aleatória
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;
    
    @Size(max = 140, message = "Descrição deve ter no máximo 140 caracteres")
    private String descricao;
    
    @NotBlank(message = "Beneficiário é obrigatório")
    @Size(max = 50, message = "Beneficiário deve ter no máximo 50 caracteres")
    private String beneficiario;

    public PixRequest() {}

    // Getters e Setters
    public String getChave() { return chave; }
    public void setChave(String chave) { this.chave = chave; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getBeneficiario() { return beneficiario; }
    public void setBeneficiario(String beneficiario) { this.beneficiario = beneficiario; }
}
