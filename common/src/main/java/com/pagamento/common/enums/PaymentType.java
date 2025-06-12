package com.pagamento.common.enums;

public enum PaymentType {
    CREDIT_CARD,
    DEBIT_CARD,
    PIX,
    BOLETO,
    TRANSFER, // Adicionado para mapear PLATFORM
    PLATFORM  // Adicionado para manter consistência se necessário
}