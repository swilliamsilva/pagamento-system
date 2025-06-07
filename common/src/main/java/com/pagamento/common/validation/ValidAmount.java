package com.pagamento.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
//@Constraint(validatedBy = AmountValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidAmount {
    String message() default "Valor deve estar entre {min} e {max}";
    
    /**
     * Valor mínimo permitido (inclusive)
     */
    double min() default 0.01;
    
    /**
     * Valor máximo permitido (inclusive)
     */
    double max() default 999999.99;
    
    /**
     * Define se o valor pode ser zero
     */
    boolean allowZero() default false;
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}