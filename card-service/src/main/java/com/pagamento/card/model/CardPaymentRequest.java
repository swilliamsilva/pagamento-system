package com.pagamento.card.model;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CardPaymentRequest {
    @NotBlank(message = "Número do cartão é obrigatório")
    @Pattern(regexp = "^\\d{16}$", message = "Número do cartão inválido")
    private String cardNumber;
    
    @NotBlank(message = "Titular do cartão é obrigatório")
    @Size(min = 3, max = 50, message = "Titular deve ter entre 3 e 50 caracteres")
    private String cardHolder;
    
    @NotBlank(message = "Data de expiração é obrigatória")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Formato inválido (MM/AA)")
    private String expiryDate; // MM/YY
    
    @NotBlank(message = "CVV é obrigatório")
    @Pattern(regexp = "^\\d{3,4}$", message = "CVV inválido (3 ou 4 dígitos)")
    private String cvv;
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;
    
    @Size(max = 140, message = "Descrição deve ter no máximo 140 caracteres")
    private String description;

    public CardPaymentRequest() {}

    // Getters e Setters
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getCardHolder() { return cardHolder; }
    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }
    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
