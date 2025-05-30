package com.pagamento.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AmountValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAmount {
    String message() default "Valor monetário inválido";
    double min() default 0.01;
    double max() default 1000000.00;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
