package com.pagamento.common.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public abstract class ExpiryDateValidator implements ConstraintValidator {
	/*
	 * The type ConstraintValidator cannot be a superinterface of ExpiryDateValidator; a superinterface must be an interface
	 * **/
	
	

    private static final Pattern DATE_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/\\d{2}$");
    private static final DateFormat FORMATTER = new SimpleDateFormat("MM/yy");
    
    static {
        FORMATTER.setLenient(false);
    }

    public void initialize(ValidExpiryDate constraintAnnotation) {
        // Nada a inicializar
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            setValidationError(context, "A data de validade não pode ser nula ou vazia");
            return false;
        }

        if (!DATE_PATTERN.matcher(value).matches()) {
            setValidationError(context, "Formato inválido. Use MM/AA (ex: 12/25)");
            return false;
        }

        try {
            Date expiryDate = FORMATTER.parse(value);
            Date currentDate = new Date();
            
            // Considera apenas mês/ano para comparação
            Calendar expiryCal = Calendar.getInstance();
            expiryCal.setTime(expiryDate);
            expiryCal.set(Calendar.DAY_OF_MONTH, 1);
            
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTime(currentDate);
            currentCal.set(Calendar.DAY_OF_MONTH, 1);
            
            if (expiryCal.before(currentCal)) {
                setValidationError(context, "O cartão está expirado");
                return false;
            }
            
            return true;
        } catch (ParseException e) {
            setValidationError(context, "Data de validade inválida");
            return false;
        }
    }

    private void setValidationError(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
               .addConstraintViolation();
        /*
         * 
         * The method addConstraintViolation() is undefined for the type Object
         * **/
        
        
    }
}