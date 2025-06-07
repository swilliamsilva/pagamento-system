package com.pagamento.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
// @Constraint(validatedBy = CardValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidCard {
    String message() default "Número de cartão inválido";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    /**
     * Define se deve validar o algoritmo de Luhn
     * @return true se deve validar o checksum
     */
    boolean validateLuhn() default true;
    
    /**
     * Define se deve validar as bandeiras conhecidas
     * @return true se deve validar o prefixo do cartão
     */
    boolean validateCardType() default true;
}