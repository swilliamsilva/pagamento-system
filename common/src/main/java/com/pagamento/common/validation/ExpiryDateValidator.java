package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, String> {

    @Override
    public void initialize(ValidExpiryDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String expiryDate, ConstraintValidatorContext context) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return false;
        }

        // Formato esperado: MM/yy
        if (!expiryDate.matches("(0[1-9]|1[0-2])/[0-9]{2}")) {
            return false;
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiry = YearMonth.parse(expiryDate, formatter);
            YearMonth current = YearMonth.now();
            
            // Verificar se a data não expirou (inclui o mês atual)
            return !expiry.isBefore(current);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
