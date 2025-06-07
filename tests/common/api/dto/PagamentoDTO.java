package com.pagamento.api.dto;

import com.pagamento.common.validation.ValidAmount;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class PagamentoDTO {

    @NotBlank(message = "ID do pedido é obrigatório")
    private String pedidoId;

    @ValidAmount(min = 5.0, max = 5000.0, message = "Valor deve estar entre R$ 5,00 e R$ 5000,00")
    private BigDecimal valor;

    public String getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(String pedidoId) {
        this.pedidoId = pedidoId;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}