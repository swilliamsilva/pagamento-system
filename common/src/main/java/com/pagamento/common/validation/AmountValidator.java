package com.pagamento.common.validation;

import java.math.BigDecimal;

public class AmountValidator {
    
    public static boolean isValid(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }
}
