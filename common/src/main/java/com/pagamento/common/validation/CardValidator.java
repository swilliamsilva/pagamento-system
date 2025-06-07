package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CardValidator implements ConstraintValidator<ValidCard, String> {
/*
 * The type ConstraintValidator is not generic; it cannot be parameterized with arguments <ValidCard, String>
 * */
	
	
	
    public void initialize(ValidCard constraintAnnotation) {
        // Nada a inicializar
    }


    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return buildViolation(context, "Número do cartão não pode ser nulo ou vazio");
        }

        String sanitized = cardNumber.replaceAll("[\\s-]", "");

        // Verifica se contém apenas dígitos
        if (!sanitized.matches("\\d+")) {
            return buildViolation(context, "Número do cartão deve conter apenas dígitos");
        }

        // Verifica o tamanho do número
        if (sanitized.length() < 13 || sanitized.length() > 19) {
            return buildViolation(context, "Número do cartão deve conter entre 13 e 19 dígitos");
        }

        // Verifica a bandeira
        if (!isValidCardType(sanitized)) {
            return buildViolation(context, "Bandeira de cartão não suportada");
        }

        // Verifica algoritmo de Luhn
        if (!isValidLuhn(sanitized)) {
            return buildViolation(context, "Número do cartão inválido (falha na verificação Luhn)");
        }

        return true;
    }

    private boolean isValidCardType(String number) {
        // Valida prefixos de bandeiras conhecidas:
        // Visa (4), Mastercard (51-55), Amex (34,37), Discover (6011, 65)
        return number.matches("^(4\\d{12}(?:\\d{3})?|" +           // Visa
                "5[1-5]\\d{14}|" +                                 // Mastercard
                "3[47]\\d{13}|" +                                  // American Express
                "6(?:011|5\\d{2})\\d{12})$");                      // Discover
    }

    private boolean isValidLuhn(String number) {
        int sum = 0;
        boolean alternate = false;
        
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return sum % 10 == 0;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        /*
         * 
         * The method addConstraintViolation() is undefined for the type Object
         * **/      
        return false;
    }
}