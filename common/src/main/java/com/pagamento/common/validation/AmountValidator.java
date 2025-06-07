package com.pagamento.common.validation;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount, BigDecimal> {
/*
 * The type ConstraintValidator is not generic; it cannot be parameterized with arguments <ValidAmount, BigDecimal>
 * */
    private BigDecimal minValue;
    private BigDecimal maxValue;


    public void initialize(ValidAmount constraintAnnotation) {
        this.minValue = BigDecimal.valueOf(constraintAnnotation.min());
        this.maxValue = BigDecimal.valueOf(constraintAnnotation.max());
    }

    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            buildViolation(context, "Valor não pode ser nulo");
            return false;
        }

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            buildViolation(context, "O valor deve ser positivo");
            return false;
        }

        if (value.compareTo(minValue) < 0) {
            buildViolation(context, "O valor mínimo permitido é " + minValue);
            return false;
        }

        if (value.compareTo(maxValue) > 0) {
            buildViolation(context, "O valor máximo permitido é " + maxValue);
            return false;
        }

        return true;
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        /**
         * The method addConstraintViolation() is undefined for the type Object
         * */
        
        
    }
}