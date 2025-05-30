package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class AmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {

    private BigDecimal minValue;
    private BigDecimal maxValue;

    @Override
    public void initialize(ValidAmount constraintAnnotation) {
        this.minValue = BigDecimal.valueOf(constraintAnnotation.min());
        this.maxValue = BigDecimal.valueOf(constraintAnnotation.max());
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Valor não pode ser nulo")
                   .addConstraintViolation();
            return false;
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("O valor deve ser positivo")
                   .addConstraintViolation();
            return false;
        }

        if (value.compareTo(minValue) < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("O valor mínimo permitido é " + minValue)
                   .addConstraintViolation();
            return false;
        }

        if (value.compareTo(maxValue) > 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("O valor máximo permitido é " + maxValue)
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}
