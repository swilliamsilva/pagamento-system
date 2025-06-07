package com.pagamento.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
//@Constraint(validatedBy = ExpiryDateValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidExpiryDate {

    String message() default "Data de validade deve estar no formato MM/AA e não pode estar expirada";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Define se deve validar a data em relação à data atual
     * @return true se deve verificar se a data é futura
     */
    boolean checkFuture() default true;
}