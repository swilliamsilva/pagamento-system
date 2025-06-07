package com.pagamento.common.dto;

import com.pagamento.common.validation.ValidExpiryDate;

public class CartaoDTO {

    @ValidExpiryDate
    private String validade; // Exemplo: "09/26"

    // getters e setters
}