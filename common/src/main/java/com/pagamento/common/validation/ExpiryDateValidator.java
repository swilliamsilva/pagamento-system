package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ExpiryDateValidator implements ConstraintValidator<ValidExpiryDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM/yy");

    @Override
    public void initialize(ValidExpiryDate constraintAnnotation) {
        // Nenhuma inicialização necessária
    }

    @Override
    public boolean isValid(String expiryDate, ConstraintValidatorContext context) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return false;
        }

        if (!expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            return false;
        }

        try {
            YearMonth expiry = YearMonth.parse(expiryDate, FORMATTER);
            YearMonth now = YearMonth.now();
            return !expiry.isBefore(now);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
