package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFValidator implements ConstraintValidator<ValidCPF, String> {
/*
 * The type ConstraintValidator is not generic; it cannot be parameterized with arguments <ValidCPF, String>
 * 
 * **/
 
    public void initialize(ValidCPF constraintAnnotation) {
        // Inicialização, se necessário
    }

    
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.trim().isEmpty()) {
            buildViolation(context, "CPF não pode ser nulo ou vazio");
            return false;
        }

        String cleaned = cpf.replaceAll("[^\\d]", "");
        if (cleaned.length() != 11) {
            buildViolation(context, "CPF deve conter 11 dígitos");
            return false;
        }

        if (allDigitsEqual(cleaned)) {
            buildViolation(context, "CPF inválido (todos dígitos iguais)");
            return false;
        }

        try {
            if (!isValidCPF(cleaned)) {
                buildViolation(context, "CPF inválido");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            buildViolation(context, "CPF contém caracteres inválidos");
            return false;
        }
    }

    private boolean allDigitsEqual(String cpf) {
        return cpf.chars().allMatch(c -> c == cpf.charAt(0));
    }

    private boolean isValidCPF(String cpf) {
        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        // Cálculo do primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += digits[i] * (10 - i);
        }
        int firstDigit = (sum % 11) < 2 ? 0 : 11 - (sum % 11);
        if (firstDigit != digits[9]) {
            return false;
        }

        // Cálculo do segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += digits[i] * (11 - i);
        }
        int secondDigit = (sum % 11) < 2 ? 0 : 11 - (sum % 11);

        return secondDigit == digits[10];
    }

    private void buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        
        /**
         * The method addConstraintViolation() is undefined for the type Object
         * 
         * */
        
    }
}