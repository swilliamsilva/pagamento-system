package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CardValidator implements ConstraintValidator<ValidCard, String> {

    @Override
    public void initialize(ValidCard constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cardNumber, ConstraintValidatorContext context) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }

        // Remover espaços e traços
        String cleanCard = cardNumber.replaceAll("\s+|-", "");

        // Verificar se é numérico e tem comprimento válido
        if (!cleanCard.matches("\d{13,19}")) {
            return false;
        }

        // Algoritmo de Luhn
        int sum = 0;
        boolean alternate = false;
        for (int i = cleanCard.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cleanCard.substring(i, i + 1));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
}
