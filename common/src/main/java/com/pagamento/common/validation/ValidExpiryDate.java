package com.pagamento.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExpiryDateValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExpiryDate {
    String message() default "Data de expiração inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
