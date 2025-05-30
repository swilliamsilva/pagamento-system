package com.pagamento.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CardValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCard {
    String message() default "Cartão de crédito inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
