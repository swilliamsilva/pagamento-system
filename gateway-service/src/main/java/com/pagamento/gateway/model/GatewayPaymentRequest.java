package com.pagamento.gateway.model;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class GatewayPaymentRequest {
    @Size(max = 20, message = "Provedor deve ter no máximo 20 caracteres")
    private String provider; // PICPAY, PAGSEGURO, MERCADOPAGO, PAYPAL, etc.
    
    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;
    
    @NotBlank(message = "Moeda é obrigatória")
    @Size(min = 3, max = 3, message = "Moeda deve ter 3 caracteres")
    private String currency;
    
    @NotBlank(message = "ID de referência é obrigatório")
    @Size(max = 50, message = "ID de referência deve ter no máximo 50 caracteres")
    private String referenceId;
    
    @NotBlank(message = "ID do cliente é obrigatório")
    @Size(max = 50, message = "ID do cliente deve ter no máximo 50 caracteres")
    private String customerId;
    
    @NotBlank(message = "Método de pagamento é obrigatório")
    @Size(max = 20, message = "Método de pagamento deve ter no máximo 20 caracteres")
    private String paymentMethod; // CREDIT_CARD, PIX, BOLETO, etc.
    
    private String metadata; // Dados específicos do gateway

    // Getters e Setters
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getReferenceId() { return referenceId; }
    public void setReferenceId(String referenceId) { this.referenceId = referenceId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}
