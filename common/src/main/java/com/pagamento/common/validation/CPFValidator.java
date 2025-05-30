package com.pagamento.common.validation;

public class CPFValidator {
    
    public static boolean isValid(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return false;
        }
        
        // Algoritmo de validação de CPF
        try {
            int[] digits = new int[11];
            for (int i = 0; i < 11; i++) {
                digits[i] = Character.getNumericValue(cpf.charAt(i));
            }
            
            // Cálculo do primeiro dígito verificador
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += digits[i] * (10 - i);
            }
            int remainder = sum % 11;
            int expectedDigit1 = (remainder < 2) ? 0 : 11 - remainder;
            
            if (digits[9] != expectedDigit1) {
                return false;
            }
            
            // Cálculo do segundo dígito verificador
            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += digits[i] * (11 - i);
            }
            remainder = sum % 11;
            int expectedDigit2 = (remainder < 2) ? 0 : 11 - remainder;
            
            return digits[10] == expectedDigit2;
            
        } catch (Exception e) {
            return false;
        }
    }
}
